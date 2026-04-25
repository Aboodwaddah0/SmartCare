package com.example.SmartCare.repository;

import com.example.SmartCare.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<DoctorSchedule, Long> {



  Optional<DoctorSchedule> findByDoctorIdAndScheduleDate(Long doctorId, LocalDate date);
 boolean existsByDoctorIdAndScheduleDate(Long doctorId,LocalDate date);

}
