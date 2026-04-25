package com.example.SmartCare.repository;

import com.example.SmartCare.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository  extends JpaRepository<Appointment, Long> {
    @Query("SELECT a.time FROM Appointment a " +
            "WHERE a.doctor.id = :doctorId AND a.date = :date")
    List<LocalTime> findBookedSlots(@Param("doctorId") Long doctorId,
                                    @Param("date") LocalDate date);
    boolean existsByDoctorIdAndDateAndTime(Long doctorId, LocalDate date, LocalTime time);
    List<Appointment> findByDoctorIdAndDateOrderByTimeAsc(Long doctorId, LocalDate date);
}
