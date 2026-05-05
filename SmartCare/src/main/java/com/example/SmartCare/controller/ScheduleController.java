package com.example.SmartCare.controller;


import com.example.SmartCare.dto.ScheduledDto;
import com.example.SmartCare.entity.Doctor;
import com.example.SmartCare.exception.ResourceNotFoundException;
import com.example.SmartCare.repository.DoctorRepository;
import com.example.SmartCare.service.DoctorScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {

    private final DoctorScheduleService  doctorScheduleService;
 private final DoctorRepository doctorRepository;
    public ScheduleController(DoctorScheduleService doctorScheduleService, DoctorRepository doctorRepository) {
        this.doctorScheduleService = doctorScheduleService;
        this.doctorRepository = doctorRepository;
    }
    @PostMapping("/{doctorId}")
    public ResponseEntity<ScheduledDto.ScheduleResponse> createSchedule ( @PathVariable Long doctorId , @RequestBody ScheduledDto.ScheduleRequest request){
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

        ScheduledDto.ScheduleResponse response =
                doctorScheduleService.createSchedule(doctorId, request);

        return ResponseEntity.ok(response);
 }

    @GetMapping("/{doctorId}/")
    public ResponseEntity<ScheduledDto.ScheduleResponse> getDoctorSchedule(
            @PathVariable Long doctorId,
            @RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        ScheduledDto.ScheduleResponse response =
                doctorScheduleService.getDoctorSchedule(doctorId, date);

        return ResponseEntity.ok(response);
    }


}
