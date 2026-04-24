package com.example.SmartCare.repository;

import com.example.SmartCare.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorSchedule, Long> {
  Optional<LocalTime> findBookedSlotsByDoctorIdAndDayOfWeek(Long doctorId, LocalDate date);
  Optional<DoctorSchedule> findByDoctorIdAndDayOfWeek(Long doctorId, LocalDate date);
}
