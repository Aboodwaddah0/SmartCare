package com.example.appointment.controller;

import com.example.appointment.dto.ScheduledDto;
import com.example.appointment.service.DoctorScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v2/schedule")
public class ScheduleController {

	private final DoctorScheduleService doctorScheduleService;

	public ScheduleController(DoctorScheduleService doctorScheduleService) {
		this.doctorScheduleService = doctorScheduleService;
	}

	@PostMapping("/{doctorId}")
	public ResponseEntity<ScheduledDto.ScheduleResponse> createSchedule(
			@PathVariable Long doctorId,
			@RequestBody ScheduledDto.ScheduleRequest request) {
		ScheduledDto.ScheduleResponse response = doctorScheduleService.createSchedule(doctorId, request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{doctorId}")
	public ResponseEntity<ScheduledDto.ScheduleResponse> getDoctorSchedule(
			@PathVariable Long doctorId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		ScheduledDto.ScheduleResponse response = doctorScheduleService.getDoctorSchedule(doctorId, date);
		return ResponseEntity.ok(response);
	}
}
