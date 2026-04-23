package com.example.SmartCare.repository;

import com.example.SmartCare.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {
  Optional<LocalTime> findBookedSlotsByDoctorIdAndDayOfWeek(Long doctorId, LocalDate date);
  Optional<DoctorAvailability> findByDoctorIdAndDayOfWeek(Long doctorId, LocalDate date);
}
