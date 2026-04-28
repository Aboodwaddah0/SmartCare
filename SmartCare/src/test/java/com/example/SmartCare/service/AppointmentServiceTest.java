package com.example.SmartCare.service;

import com.example.SmartCare.dto.AppointmentDto;
import com.example.SmartCare.entity.*;
import com.example.SmartCare.exception.*;
import com.example.SmartCare.repository.AppointmentRepository;
import com.example.SmartCare.repository.DoctorRepository;
import com.example.SmartCare.repository.PatientRepository;
import com.example.SmartCare.repository.ScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private DoctorScheduleService doctorScheduleService;

    @InjectMocks
    private AppointmentService appointmentService;

    private Doctor doctor;
    private Patient patient;
    private DoctorSchedule schedule;
    private LocalDate testDate;
    private LocalTime testTime;

    @BeforeEach
    void setUp() {
        User doctorUser = User.builder().id(1L).fullName("Dr. Smith").username("drsmith").email("dr@example.com").role(com.example.SmartCare.entity.Role.DOCTOR).build();
        doctor = Doctor.builder().id(1L).user(doctorUser).specialty("Cardiology").build();

        User patientUser = User.builder().id(2L).fullName("John Doe").username("johndoe").email("john@example.com").role(com.example.SmartCare.entity.Role.PATIENT).build();
        patient = Patient.builder().id(1L).user(patientUser).gender("Male").build();

        testDate = LocalDate.now().plusDays(1);
        testTime = LocalTime.of(10, 0);

        schedule = DoctorSchedule.builder()
                .id(1L)
                .doctor(doctor)
                .scheduleDate(testDate)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .duration(30)
                .build();
    }

    @Test
    void bookAppointment_Success() {
        AppointmentDto.BookingRequest request = AppointmentDto.BookingRequest.builder()
                .doctorId(1L)
                .patientId(1L)
                .date(testDate)
                .time(testTime)
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(scheduleRepository.findByDoctorIdAndScheduleDate(1L, testDate)).thenReturn(Optional.of(schedule));
        when(appointmentRepository.existsByDoctorIdAndDateAndTime(1L, testDate, testTime)).thenReturn(false);

        assertDoesNotThrow(() -> appointmentService.bookAppointment(request));

        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void bookAppointment_DoctorNotFound() {
        AppointmentDto.BookingRequest request = AppointmentDto.BookingRequest.builder()
                .doctorId(999L)
                .patientId(1L)
                .date(testDate)
                .time(testTime)
                .build();

        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.bookAppointment(request));
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void bookAppointment_PatientNotFound() {
        AppointmentDto.BookingRequest request = AppointmentDto.BookingRequest.builder()
                .doctorId(1L)
                .patientId(999L)
                .date(testDate)
                .time(testTime)
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.bookAppointment(request));
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void bookAppointment_NoSchedule() {
        AppointmentDto.BookingRequest request = AppointmentDto.BookingRequest.builder()
                .doctorId(1L)
                .patientId(1L)
                .date(testDate)
                .time(testTime)
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(scheduleRepository.findByDoctorIdAndScheduleDate(1L, testDate)).thenReturn(Optional.empty());

        assertThrows(NoScheduleException.class, () -> appointmentService.bookAppointment(request));
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void bookAppointment_InvalidSlot() {
        AppointmentDto.BookingRequest request = AppointmentDto.BookingRequest.builder()
                .doctorId(1L)
                .patientId(1L)
                .date(testDate)
                .time(LocalTime.of(8, 0))
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(scheduleRepository.findByDoctorIdAndScheduleDate(1L, testDate)).thenReturn(Optional.of(schedule));

        assertThrows(InvalidSlotException.class, () -> appointmentService.bookAppointment(request));
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void bookAppointment_DoubleBooking_ThrowsSlotAlreadyTakenException() {
        AppointmentDto.BookingRequest request = AppointmentDto.BookingRequest.builder()
                .doctorId(1L)
                .patientId(1L)
                .date(testDate)
                .time(testTime)
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(scheduleRepository.findByDoctorIdAndScheduleDate(1L, testDate)).thenReturn(Optional.of(schedule));
        when(appointmentRepository.existsByDoctorIdAndDateAndTime(1L, testDate, testTime)).thenReturn(true);

        assertThrows(SlotAlreadyTakenException.class, () -> appointmentService.bookAppointment(request));
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void cancelAppointment_Success() {
        Appointment appointment = Appointment.builder()
                .id(1L)
                .doctor(doctor)
                .patient(patient)
                .date(testDate)
                .time(testTime)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        assertDoesNotThrow(() -> appointmentService.cancel(1L));

        assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
        verify(appointmentRepository, times(1)).save(appointment);
    }

    @Test
    void cancelAppointment_NotFound() {
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.cancel(999L));
    }

    @Test
    void cancelAppointment_AlreadyCompleted() {
        Appointment appointment = Appointment.builder()
                .id(1L)
                .doctor(doctor)
                .patient(patient)
                .date(testDate)
                .time(testTime)
                .status(AppointmentStatus.COMPLETED)
                .build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        BusinessException exception = assertThrows(BusinessException.class, () -> appointmentService.cancel(1L));
        assertEquals("Cannot cancel a completed appointment", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void cancelAppointment_AlreadyCancelled() {
        Appointment appointment = Appointment.builder()
                .id(1L)
                .doctor(doctor)
                .patient(patient)
                .date(testDate)
                .time(testTime)
                .status(AppointmentStatus.CANCELLED)
                .build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        BusinessException exception = assertThrows(BusinessException.class, () -> appointmentService.cancel(1L));
        assertEquals("Appointment already cancelled", exception.getMessage());
    }

    @Test
    void cancelAppointment_SameDay_ShouldFail() {
        Appointment appointment = Appointment.builder()
                .id(1L)
                .doctor(doctor)
                .patient(patient)
                .date(LocalDate.now())
                .time(testTime)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        BusinessException exception = assertThrows(BusinessException.class, () -> appointmentService.cancel(1L));
        assertEquals("Cannot cancel appointment on the same day or past", exception.getMessage());
    }

    @Test
    void getAvailableSlots_Success() {
        when(scheduleRepository.findByDoctorIdAndScheduleDate(1L, testDate)).thenReturn(Optional.of(schedule));
        when(doctorScheduleService.generateSlots(schedule)).thenReturn(new java.util.ArrayList<>(List.of(LocalTime.of(9, 0), LocalTime.of(9, 30), testTime, LocalTime.of(10, 30))));
        when(appointmentRepository.findBookedSlots(1L, testDate)).thenReturn(List.of(testTime));

        List<LocalTime> availableSlots = appointmentService.getAvailableSlots(1L, testDate);

        assertEquals(3, availableSlots.size());
        assertFalse(availableSlots.contains(testTime));
    }

    @Test
    void getAvailableSlots_NoSchedule() {
        when(scheduleRepository.findByDoctorIdAndScheduleDate(1L, testDate)).thenReturn(Optional.empty());

        assertThrows(NoScheduleException.class, () -> appointmentService.getAvailableSlots(1L, testDate));
    }

    @Test
    void getAllAppointments_Success() {
        Appointment appointment = Appointment.builder()
                .id(1L)
                .doctor(doctor)
                .patient(patient)
                .date(testDate)
                .time(testTime)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorIdAndDateOrderByTimeAsc(1L, testDate)).thenReturn(List.of(appointment));

        var responses = appointmentService.getAllAppointments(1L, testDate);

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals("SCHEDULED", responses.get(0).getStatus());
    }
}
