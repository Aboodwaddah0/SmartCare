package com.example.SmartCare.service;


import com.example.SmartCare.dto.AppointmentDto;
import com.example.SmartCare.entity.*;
import com.example.SmartCare.repository.AppointmentRepository;
import com.example.SmartCare.repository.DoctorAvailabilityRepository;
import com.example.SmartCare.repository.DoctorRepository;
import com.example.SmartCare.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AppointmentService {

private final DoctorRepository doctorRepository;
private final PatientRepository patientRepository;
private final AppointmentRepository appointmentRepository;

public AppointmentService(DoctorRepository doctorRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository, DoctorAvailabilityRepository doctorAvailabilityRepository) {
    this.doctorRepository = doctorRepository;
    this.patientRepository = patientRepository;
    this.appointmentRepository = appointmentRepository;

}


public Appointment  bookAppointment(AppointmentDto.BookingRequest request){

    Doctor doctor = doctorRepository.findById(request.getDoctorId())
            .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + request.getDoctorId()));

    Patient patient = patientRepository.findById(request.getPatientId())
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + request.getPatientId()));


    boolean exists = appointmentRepository
            .existsByDoctorIdAndDateAndTime(request.getDoctorId(), request.getDate(), request.getTime());

    if (exists) {
        throw new RuntimeException("Time slot already taken");
    }

    Appointment appointment = Appointment.builder()
            .doctor(doctor)
            .patient(patient)
            .date(request.getDate())
            .time(request.getTime())
            .status(AppointmentStatus.SCHEDULED)
            .build();

    return appointmentRepository.save(appointment);
}


    public void cancel(Long appointmentId) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Appointment already cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        appointmentRepository.save(appointment);
    }












}
