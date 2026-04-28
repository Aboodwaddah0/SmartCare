package com.example.SmartCare.controller;

import com.example.SmartCare.dto.AppointmentDto;
import com.example.SmartCare.dto.ApiResponse;
import com.example.SmartCare.service.AppointmentService;
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
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void bookAppointment_Success() throws Exception {
        AppointmentDto.BookingRequest request = AppointmentDto.BookingRequest.builder()
                .doctorId(1L)
                .patientId(1L)
                .date(LocalDate.now().plusDays(1))
                .time(LocalTime.of(10, 0))
                .build();

        doNothing().when(appointmentService).bookAppointment(any(AppointmentDto.BookingRequest.class));

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Appointment booked successfully"));

        verify(appointmentService, times(1)).bookAppointment(any(AppointmentDto.BookingRequest.class));
    }

    @Test
    void cancelAppointment_Success() throws Exception {
        doNothing().when(appointmentService).cancel(anyLong());

        mockMvc.perform(patch("/api/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Appointment cancelled successfully"));

        verify(appointmentService, times(1)).cancel(1L);
    }

    @Test
    void getAppointmentsDoctor_Success() throws Exception {
        LocalDate date = LocalDate.now().plusDays(1);
        AppointmentDto.AppointmentResponse response = AppointmentDto.AppointmentResponse.builder()
                .id(1L)
                .doctorId(1L)
                .patientId(1L)
                .date(date)
                .time(LocalTime.of(10, 0))
                .status("SCHEDULED")
                .build();

        when(appointmentService.getAllAppointments(anyLong(), any(LocalDate.class)))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/appointments/doctor/1")
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("SCHEDULED"));
    }

    @Test
    void getAvailableSlots_Success() throws Exception {
        LocalDate date = LocalDate.now().plusDays(1);
        List<LocalTime> slots = List.of(LocalTime.of(9, 0), LocalTime.of(9, 30), LocalTime.of(10, 0));

        when(appointmentService.getAvailableSlots(anyLong(), any(LocalDate.class)))
                .thenReturn(slots);

        mockMvc.perform(get("/api/appointments/available-slots/1")
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }
}
