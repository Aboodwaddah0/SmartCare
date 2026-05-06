package com.example.appointment.service;

import com.example.appointment.dto.AppointmentDto;
import com.example.appointment.entity.Appointment;
import com.example.appointment.entity.AppointmentStatus;
import com.example.appointment.entity.DoctorSchedule;
import com.example.appointment.exception.*;
import com.example.appointment.repository.AppointmentRepository;
import com.example.appointment.repository.ScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AppointmentService {

	private final AppointmentRepository appointmentRepository;
	private final ScheduleRepository scheduleRepository;

	public AppointmentService(AppointmentRepository appointmentRepository, ScheduleRepository scheduleRepository) {
		this.appointmentRepository = appointmentRepository;
		this.scheduleRepository = scheduleRepository;
	}

	@Transactional
	public void bookAppointment(AppointmentDto.BookingRequest request) {
		DoctorSchedule schedule = scheduleRepository.findByDoctorIdAndScheduleDate(request.getDoctorId(), request.getDate())
				.orElseThrow(() -> new NoScheduleException("Doctor has no schedule on " + request.getDate()));

		if (!isSlotValid(schedule, request.getTime())) {
			throw new InvalidSlotException("Time slot is not in doctor's schedule");
		}

		boolean exists = appointmentRepository.existsByDoctorIdAndDateAndTime(request.getDoctorId(), request.getDate(), request.getTime());
		if (exists) {
			throw new SlotAlreadyTakenException("Time slot already taken");
		}

		Appointment appointment = Appointment.builder()
				.doctorId(request.getDoctorId())
				.patientId(request.getPatientId())
				.date(request.getDate())
				.time(request.getTime())
				.status(AppointmentStatus.SCHEDULED)
				.build();

		try {
			appointmentRepository.save(appointment);
		} catch (DataIntegrityViolationException e) {
			throw new SlotAlreadyTakenException("Time slot already taken (concurrent booking)");
		}
	}

	public void cancel(Long appointmentId) {
		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

		if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
			throw new BusinessException("Cannot cancel a completed appointment", org.springframework.http.HttpStatus.BAD_REQUEST);
		}
		if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
			throw new BusinessException("Appointment already cancelled", org.springframework.http.HttpStatus.BAD_REQUEST);
		}
		if (!appointment.getDate().isAfter(LocalDate.now())) {
			throw new BusinessException("Cannot cancel appointment on the same day or past", org.springframework.http.HttpStatus.BAD_REQUEST);
		}

		appointment.setStatus(AppointmentStatus.CANCELLED);
		appointmentRepository.save(appointment);
	}

	public void complete(Long appointmentId) {
		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

		if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
			throw new BusinessException("Appointment already completed", org.springframework.http.HttpStatus.BAD_REQUEST);
		}
		if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
			throw new BusinessException("Cannot complete a cancelled appointment", org.springframework.http.HttpStatus.BAD_REQUEST);
		}

		appointment.setStatus(AppointmentStatus.COMPLETED);
		appointmentRepository.save(appointment);
	}

	public List<AppointmentDto.AppointmentResponse> getAllAppointments(Long doctorId, LocalDate date) {
		return appointmentRepository.findByDoctorIdAndDateOrderByTimeAsc(doctorId, date)
				.stream().map(this::toDto).toList();
	}

	public List<AppointmentDto.AppointmentResponse> getPatientAppointments(Long patientId) {
		return appointmentRepository.findByPatientIdOrderByDateDescTimeAsc(patientId)
				.stream().map(this::toDto).toList();
	}

	public List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date) {
		DoctorSchedule schedule = scheduleRepository.findByDoctorIdAndScheduleDate(doctorId, date)
				.orElseThrow(() -> new NoScheduleException("No schedule"));

		List<LocalTime> allSlots = generateSlots(schedule);
		List<LocalTime> bookedSlots = appointmentRepository.findBookedSlots(doctorId, date);
		allSlots.removeAll(bookedSlots);
		return allSlots;
	}

	@Scheduled(fixedRate = 60000)
	public void monitorAppointments() {
		List<Appointment> appointments = appointmentRepository.findByStatus(AppointmentStatus.SCHEDULED);
		LocalDate today = LocalDate.now();
		LocalTime nowTime = LocalTime.now();

		for (Appointment a : appointments) {
			if (a.getDate() != null && a.getTime() != null
					&& (a.getDate().isBefore(today) || (a.getDate().isEqual(today) && a.getTime().isBefore(nowTime)))) {
				a.setStatus(AppointmentStatus.COMPLETED);
				appointmentRepository.save(a);
			}
		}
	}

	private boolean isSlotValid(DoctorSchedule schedule, LocalTime requestedTime) {
		LocalTime cursor = schedule.getStartTime();
		while (cursor.isBefore(schedule.getEndTime())) {
			if (cursor.equals(requestedTime)) return true;
			cursor = cursor.plusMinutes(schedule.getDuration());
		}
		return false;
	}

	public List<LocalTime> generateSlots(DoctorSchedule schedule) {
		List<LocalTime> slots = new java.util.ArrayList<>();
		LocalTime current = schedule.getStartTime();
		while (!current.isAfter(schedule.getEndTime().minusMinutes(schedule.getDuration()))) {
			slots.add(current);
			current = current.plusMinutes(schedule.getDuration());
		}
		return slots;
	}

	private AppointmentDto.AppointmentResponse toDto(Appointment a) {
		return AppointmentDto.AppointmentResponse.builder()
				.id(a.getId())
				.doctorId(a.getDoctorId())
				.patientId(a.getPatientId())
				.date(a.getDate())
				.time(a.getTime())
				.status(a.getStatus().name())
				.build();
	}
}
