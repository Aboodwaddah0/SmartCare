package com.example.appointment.repository;

import com.example.appointment.entity.Appointment;
import com.example.appointment.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	@Query("SELECT a.time FROM Appointment a WHERE a.doctorId = :doctorId AND a.date = :date")
	List<LocalTime> findBookedSlots(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);

	boolean existsByDoctorIdAndDateAndTime(Long doctorId, LocalDate date, LocalTime time);

	List<Appointment> findByDoctorIdAndDateOrderByTimeAsc(Long doctorId, LocalDate date);

	List<Appointment> findByPatientIdOrderByDateDescTimeAsc(Long patientId);

	List<Appointment> findByStatus(AppointmentStatus status);
}
