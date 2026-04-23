package com.example.SmartCare.repository;

import com.example.SmartCare.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AppointmentRepository  extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorIdAndDateAndTime(Long doctorId, LocalDate date, LocalTime time);
}
