package com.example.SmartCare.service;


import com.example.SmartCare.dto.AppointmentDto;
import com.example.SmartCare.entity.*;
import com.example.SmartCare.exception.*;
import com.example.SmartCare.repository.AppointmentRepository;
import com.example.SmartCare.repository.DoctorRepository;
import com.example.SmartCare.repository.PatientRepository;
import com.example.SmartCare.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class AppointmentService {

private final DoctorRepository doctorRepository;
private final PatientRepository patientRepository;
private final AppointmentRepository appointmentRepository;
private final ScheduleRepository scheduleRepository;
private final DoctorScheduleService doctorScheduleService;
public AppointmentService(DoctorRepository doctorRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository, ScheduleRepository scheduleRepository, DoctorScheduleService doctorScheduleService) {
    this.doctorRepository = doctorRepository;
    this.patientRepository = patientRepository;
    this.appointmentRepository = appointmentRepository;
    this.scheduleRepository = scheduleRepository;
    this.doctorScheduleService = doctorScheduleService;
}


public Appointment  bookAppointment(AppointmentDto.BookingRequest request){

    Doctor doctor = doctorRepository.findById(request.getDoctorId())
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));

    Patient patient = patientRepository.findById(request.getPatientId())
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));

    DoctorSchedule schedule = scheduleRepository.findByDoctorIdAndScheduleDate(request.getDoctorId(),request.getDate()).orElseThrow(() -> new NoScheduleException("Doctor has no schedule on " + request.getDate()));

    if (!isSlotValid(schedule, request.getTime())) {
        throw new InvalidSlotException("Time slot is not in doctor's schedule");
    }

    boolean exists = appointmentRepository
            .existsByDoctorIdAndDateAndTime(request.getDoctorId(), request.getDate(), request.getTime());

    if (exists) {
        throw new SlotAlreadyTakenException("Time slot already taken");
    }

    Appointment appointment = Appointment.builder()
            .doctor(doctor)
            .patient(patient)
            .date(request.getDate())
            .time(request.getTime())
            .status(AppointmentStatus.SCHEDULED)
            .build();

    try {
        return appointmentRepository.save(appointment);
    } catch (DataIntegrityViolationException e) {
        throw new SlotAlreadyTakenException("Time slot already taken (concurrent booking)");
    }
}


    public void cancel(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BusinessException("Cannot cancel a completed appointment", HttpStatus.BAD_REQUEST);
        }


        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BusinessException("Appointment already cancelled", HttpStatus.BAD_REQUEST);
        }

        if (!appointment.getDate().isAfter(LocalDate.now())) {
            throw new BusinessException("Cannot cancel appointment on the same day or past", HttpStatus.BAD_REQUEST);
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }



    public List<Appointment> getAllAppointments(Long doctorId, LocalDate date){

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

     return appointmentRepository.findByDoctorIdAndDateOrderByTimeAsc(doctorId,date).stream().toList();


    }

    public List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date) {

        DoctorSchedule schedule = scheduleRepository
                .findByDoctorIdAndScheduleDate(doctorId, date)
                .orElseThrow(() -> new NoScheduleException("No schedule"));

        List<LocalTime> allSlots =
                doctorScheduleService.generateSlots(schedule);

        List<LocalTime> bookedSlots =
                appointmentRepository.findBookedSlots(doctorId, date);

        allSlots.removeAll(bookedSlots);

        return allSlots;
    }



private boolean isSlotValid(DoctorSchedule schedule, LocalTime requestedTime) {
        LocalTime cursor = schedule.getStartTime();
        while (cursor.isBefore(schedule.getEndTime())) {
            if (cursor.equals(requestedTime)) return true;
            cursor = cursor.plusMinutes(schedule.getDuration());
        }
        return false;
    }

}
