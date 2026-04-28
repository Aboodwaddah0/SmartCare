package com.example.SmartCare.controller;

import com.example.SmartCare.dto.UserDto;
import com.example.SmartCare.entity.Doctor;
import com.example.SmartCare.entity.Role;
import com.example.SmartCare.entity.User;
import com.example.SmartCare.service.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(doctorController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createDoctor_Success() throws Exception {
        UserDto.CreateDoctorRequest request = UserDto.CreateDoctorRequest.builder()
                .fullName("Dr. Smith")
                .username("drsmith")
                .password("password123")
                .email("dr@example.com")
                .phone("1234567890")
                .specialty("Cardiology")
                .experience("10 years")
                .profilePic("pic.jpg")
                .build();

        doNothing().when(doctorService).createDoctor(any(UserDto.CreateDoctorRequest.class));

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Doctor created successfully"));

        verify(doctorService, times(1)).createDoctor(any(UserDto.CreateDoctorRequest.class));
    }

    @Test
    void getDoctorById_Found() throws Exception {
        Doctor doctor = Doctor.builder()
                .id(1L)
                .user(User.builder()
                        .fullName("Dr. Smith")
                        .username("drsmith")
                        .email("dr@example.com")
                        .phone("1234567890")
                        .role(Role.DOCTOR)
                        .build())
                .specialty("Cardiology")
                .experience("10 years")
                .profilePic("pic.jpg")
                .build();

        when(doctorService.getDoctorById(1L)).thenReturn(doctor);

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Dr. Smith"))
                .andExpect(jsonPath("$.specialty").value("Cardiology"));
    }

    @Test
    void getDoctorById_NotFound() throws Exception {
        when(doctorService.getDoctorById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/doctors/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllDoctors_Success() throws Exception {
        Doctor doctor1 = Doctor.builder()
                .id(1L)
                .user(User.builder().fullName("Dr. Smith").role(Role.DOCTOR).build())
                .specialty("Cardiology")
                .build();

        Doctor doctor2 = Doctor.builder()
                .id(2L)
                .user(User.builder().fullName("Dr. Jones").role(Role.DOCTOR).build())
                .specialty("Neurology")
                .build();

        when(doctorService.getAllDoctors()).thenReturn(Arrays.asList(doctor1, doctor2));

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateDoctor_Success() throws Exception {
        Doctor doctor = Doctor.builder()
                .id(1L)
                .user(User.builder()
                        .fullName("Dr. Smith Updated")
                        .email("updated@example.com")
                        .phone("0987654321")
                        .build())
                .specialty("Cardiology Updated")
                .build();

        when(doctorService.updateDoctor(eq(1L), any(Doctor.class))).thenReturn(doctor);

        mockMvc.perform(put("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Dr. Smith Updated"))
                .andExpect(jsonPath("$.specialty").value("Cardiology Updated"));
    }

    @Test
    void deleteDoctor_Success() throws Exception {
        doNothing().when(doctorService).deleteDoctor(anyLong());

        mockMvc.perform(delete("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Doctor deleted successfully"));

        verify(doctorService, times(1)).deleteDoctor(1L);
    }

    @Test
    void getDoctorsBySpecialty_Success() throws Exception {
        Doctor doctor = Doctor.builder()
                .id(1L)
                .user(User.builder().fullName("Dr. Smith").role(Role.DOCTOR).build())
                .specialty("Cardiology")
                .build();

        when(doctorService.getDoctorsBySpecialty("Cardiology")).thenReturn(List.of(doctor));

        mockMvc.perform(get("/api/doctors/specialty").param("specialty", "Cardiology"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].specialty").value("Cardiology"));
    }
}
