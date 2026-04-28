package com.example.SmartCare.controller;

import com.example.SmartCare.dto.UserDto;
import com.example.SmartCare.entity.Patient;
import com.example.SmartCare.entity.Role;
import com.example.SmartCare.entity.User;
import com.example.SmartCare.service.PatientService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void createPatient_Success() throws Exception {
        UserDto.CreatePatientRequest request = UserDto.CreatePatientRequest.builder()
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

        doNothing().when(patientService).createPatient(any(UserDto.CreatePatientRequest.class));

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Patient created successfully"));

        verify(patientService, times(1)).createPatient(any(UserDto.CreatePatientRequest.class));
    }

    @Test
    void getPatientById_Found() throws Exception {
        Patient patient = Patient.builder()
                .id(1L)
                .user(User.builder()
                        .fullName("John Doe")
                        .username("johndoe")
                        .email("john@example.com")
                        .phone("1234567890")
                        .role(Role.PATIENT)
                        .build())
                .gender("Male")
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .build();

        when(patientService.getPatientById(1L)).thenReturn(patient);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.gender").value("Male"));
    }

    @Test
    void getPatientById_NotFound() throws Exception {
        when(patientService.getPatientById(999L)).thenReturn(null);

        mockMvc.perform(get("/api/patients/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllPatients_Success() throws Exception {
        Patient patient1 = Patient.builder()
                .id(1L)
                .user(User.builder().fullName("John Doe").role(Role.PATIENT).build())
                .gender("Male")
                .build();

        Patient patient2 = Patient.builder()
                .id(2L)
                .user(User.builder().fullName("Jane Doe").role(Role.PATIENT).build())
                .gender("Female")
                .build();

        when(patientService.getAllPatients()).thenReturn(Arrays.asList(patient1, patient2));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updatePatient_Success() throws Exception {
        Patient patientUpdate = Patient.builder()
                .user(User.builder()
                        .fullName("John Doe Updated")
                        .email("updated@example.com")
                        .phone("0987654321")
                        .build())
                .gender("Male")
                .build();

        Patient updatedPatient = Patient.builder()
                .id(1L)
                .user(User.builder()
                        .fullName("John Doe Updated")
                        .email("updated@example.com")
                        .phone("0987654321")
                        .role(Role.PATIENT)
                        .build())
                .gender("Male")
                .build();

        when(patientService.updatePatient(eq(1L), any(Patient.class))).thenReturn(updatedPatient);

        mockMvc.perform(put("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe Updated"));
    }

    @Test
    void deletePatient_Success() throws Exception {
        doNothing().when(patientService).deletePatient(anyLong());

        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Patient deleted successfully"));

        verify(patientService, times(1)).deletePatient(1L);
    }

    @Test
    void searchPatientByUsername_Success() throws Exception {
        Patient patient = Patient.builder()
                .id(1L)
                .user(User.builder()
                        .fullName("John Doe")
                        .username("johndoe")
                        .email("john@example.com")
                        .role(Role.PATIENT)
                        .build())
                .gender("Male")
                .build();

        when(patientService.getPatientByName("john")).thenReturn(List.of(patient));

        mockMvc.perform(get("/api/patients/search/john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].fullName").value("John Doe"));
    }
}
