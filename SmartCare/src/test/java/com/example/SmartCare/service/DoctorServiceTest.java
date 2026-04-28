package com.example.SmartCare.service;

import com.example.SmartCare.dto.UserDto;
import com.example.SmartCare.entity.Doctor;
import com.example.SmartCare.entity.Role;
import com.example.SmartCare.entity.User;
import com.example.SmartCare.exception.ResourceAlreadyExistsException;
import com.example.SmartCare.exception.ResourceNotFoundException;
import com.example.SmartCare.repository.DoctorRepository;
import com.example.SmartCare.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor doctor;
    private UserDto.CreateDoctorRequest createRequest;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .id(1L)
                .fullName("Dr. Smith")
                .username("drsmith")
                .email("dr@example.com")
                .phone("1234567890")
                .role(Role.DOCTOR)
                .build();

        doctor = Doctor.builder()
                .id(1L)
                .user(user)
                .specialty("Cardiology")
                .experience("10 years")
                .profilePic("pic.jpg")
                .build();

        createRequest = UserDto.CreateDoctorRequest.builder()
                .fullName("Dr. Smith")
                .username("drsmith")
                .password("password123")
                .email("dr@example.com")
                .phone("1234567890")
                .specialty("Cardiology")
                .experience("10 years")
                .profilePic("pic.jpg")
                .build();
    }

    @Test
    void createDoctor_Success() {
        when(userRepository.findByUsername("drsmith")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("dr@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        assertDoesNotThrow(() -> doctorService.createDoctor(createRequest));

        verify(userRepository, times(1)).save(any(User.class));
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void createDoctor_UsernameAlreadyExists() {
        when(userRepository.findByUsername("drsmith")).thenReturn(Optional.of(doctor.getUser()));

        assertThrows(ResourceAlreadyExistsException.class, () -> doctorService.createDoctor(createRequest));
        verify(userRepository, never()).save(any(User.class));
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void createDoctor_EmailAlreadyExists() {
        when(userRepository.findByUsername("drsmith")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("dr@example.com")).thenReturn(Optional.of(doctor.getUser()));

        assertThrows(ResourceAlreadyExistsException.class, () -> doctorService.createDoctor(createRequest));
        verify(userRepository, never()).save(any(User.class));
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void getDoctorById_Found() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        Doctor result = doctorService.getDoctorById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Dr. Smith", result.getUser().getFullName());
    }

    @Test
    void getDoctorById_NotFound() {
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        Doctor result = doctorService.getDoctorById(999L);

        assertNull(result);
    }

    @Test
    void getAllDoctors_ReturnsList() {
        Doctor doctor2 = Doctor.builder()
                .id(2L)
                .user(User.builder().fullName("Dr. Jones").role(Role.DOCTOR).build())
                .specialty("Neurology")
                .build();

        when(doctorRepository.findAll()).thenReturn(Arrays.asList(doctor, doctor2));

        List<Doctor> result = doctorService.getAllDoctors();

        assertEquals(2, result.size());
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void updateDoctor_Success() {
        Doctor updatedDetails = Doctor.builder()
                .user(User.builder().fullName("Dr. Smith Updated").email("updated@example.com").phone("0987654321").build())
                .specialty("Cardiology Updated")
                .experience("15 years")
                .profilePic("newpic.jpg")
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(userRepository.save(any(User.class))).thenReturn(doctor.getUser());
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor result = doctorService.updateDoctor(1L, updatedDetails);

        assertNotNull(result);
        verify(doctorRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void updateDoctor_NotFound() {
        Doctor updatedDetails = Doctor.builder()
                .user(User.builder().fullName("Dr. Smith Updated").build())
                .specialty("Cardiology Updated")
                .build();

        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.updateDoctor(999L, updatedDetails));
    }

    @Test
    void deleteDoctor_Success() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        assertDoesNotThrow(() -> doctorService.deleteDoctor(1L));

        verify(userRepository, times(1)).delete(doctor.getUser());
        verify(doctorRepository, times(1)).delete(doctor);
    }

    @Test
    void deleteDoctor_NotFound() {
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.deleteDoctor(999L));
        verify(userRepository, never()).delete(any(User.class));
        verify(doctorRepository, never()).delete(any(Doctor.class));
    }

    @Test
    void getDoctorsBySpecialty_ReturnsList() {
        when(doctorRepository.findBySpecialtyIgnoreCase("Cardiology")).thenReturn(Arrays.asList(doctor));

        List<Doctor> result = doctorService.getDoctorsBySpecialty("Cardiology");

        assertEquals(1, result.size());
        assertEquals("Cardiology", result.get(0).getSpecialty());
    }
}
