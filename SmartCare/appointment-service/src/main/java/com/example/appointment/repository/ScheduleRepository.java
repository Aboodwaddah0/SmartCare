package com.example.appointment.repository;

import com.example.appointment.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
	Optional<DoctorSchedule> findByDoctorIdAndScheduleDate(Long doctorId, LocalDate date);
	boolean existsByDoctorIdAndScheduleDate(Long doctorId, LocalDate date);
}
