package com.example.SmartCare.service;

import com.example.SmartCare.dto.UserDto;
import com.example.SmartCare.entity.Patient;
import com.example.SmartCare.entity.Role;
import com.example.SmartCare.entity.User;
import com.example.SmartCare.exception.ResourceAlreadyExistsException;
import com.example.SmartCare.exception.ResourceNotFoundException;
import com.example.SmartCare.repository.PatientRepository;
import com.example.SmartCare.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private UserDto.CreatePatientRequest createRequest;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .id(1L)
                .fullName("John Doe")
                .username("johndoe")
                .email("john@example.com")
                .phone("1234567890")
                .role(Role.PATIENT)
                .build();

        patient = Patient.builder()
                .id(1L)
                .user(user)
                .gender("Male")
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .address("123 Main St")
                .bloodType("O+")
                .build();

        createRequest = UserDto.CreatePatientRequest.builder()
                .fullName("John Doe")
                .username("johndoe")
                .password("password123")
                .email("john@example.com")
                .phone("1234567890")
                .gender("Male")
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .address("123 Main St")
                .bloodType("O+")
                .build();
    }

    @Test
    void createPatient_Success() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        assertDoesNotThrow(() -> patientService.createPatient(createRequest));

        verify(userRepository, times(1)).save(any(User.class));
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void createPatient_UsernameAlreadyExists() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(patient.getUser()));

        assertThrows(ResourceAlreadyExistsException.class, () -> patientService.createPatient(createRequest));
        verify(userRepository, never()).save(any(User.class));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void createPatient_EmailAlreadyExists() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(patient.getUser()));

        assertThrows(ResourceAlreadyExistsException.class, () -> patientService.createPatient(createRequest));
        verify(userRepository, never()).save(any(User.class));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void getPatientById_Found() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Patient result = patientService.getPatientById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getUser().getFullName());
    }

    @Test
    void getPatientById_NotFound() {
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        Patient result = patientService.getPatientById(999L);

        assertNull(result);
    }

    @Test
    void getAllPatients_ReturnsList() {
        Patient patient2 = Patient.builder()
                .id(2L)
                .user(User.builder().fullName("Jane Doe").role(Role.PATIENT).build())
                .gender("Female")
                .build();

        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient, patient2));

        List<Patient> result = patientService.getAllPatients();

        assertEquals(2, result.size());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void updatePatient_Success() {
        Patient updatedDetails = Patient.builder()
                .user(User.builder()
                        .fullName("John Doe Updated")
                        .email("updated@example.com")
                        .phone("0987654321")
                        .build())
                .gender("Male")
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(userRepository.save(any(User.class))).thenReturn(patient.getUser());
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient result = patientService.updatePatient(1L, updatedDetails);

        assertNotNull(result);
        verify(patientRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void updatePatient_NotFound() {
        Patient updatedDetails = Patient.builder()
                .user(User.builder().fullName("Updated").build())
                .gender("Male")
                .build();

        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patientService.updatePatient(999L, updatedDetails));
    }

    @Test
    void deletePatient_Success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        assertDoesNotThrow(() -> patientService.deletePatient(1L));

        verify(userRepository, times(1)).delete(patient.getUser());
        verify(patientRepository, times(1)).delete(patient);
    }

    @Test
    void deletePatient_NotFound() {
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patientService.deletePatient(999L));
        verify(userRepository, never()).delete(any(User.class));
        verify(patientRepository, never()).delete(any(Patient.class));
    }

    @Test
    void getPatientByName_ReturnsMatchingPatients() {
        when(patientRepository.findByFullNameContainingIgnoreCase("John")).thenReturn(Arrays.asList(patient));

        List<Patient> result = patientService.getPatientByName("John");

        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getUser().getFullName());
    }
}
