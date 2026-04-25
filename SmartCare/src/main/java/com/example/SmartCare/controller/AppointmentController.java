package com.example.SmartCare.controller;


import com.example.SmartCare.dto.AppointmentDto;
import com.example.SmartCare.entity.Appointment;
import com.example.SmartCare.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

 private final AppointmentService appointmentService;

 public AppointmentController(AppointmentService appointmentService) {
  this.appointmentService = appointmentService;
 }


 @GetMapping("/doctor/{doctorId}")
 public ResponseEntity<List<Appointment>> getAppointmentsDoctor(
         @PathVariable Long doctorId,
         @RequestParam LocalDate date) {

  return ResponseEntity.ok(
          appointmentService.getAllAppointments(doctorId, date)
  );
 }


 @PostMapping
 public ResponseEntity<Appointment> bookAppointment(
         @RequestBody AppointmentDto.BookingRequest request) {

  return ResponseEntity.ok(
          appointmentService.bookAppointment(request)
  );
 }


 @DeleteMapping("/{appointmentId}")
 public ResponseEntity<String> cancelAppointment(
         @PathVariable Long appointmentId) {

  appointmentService.cancel(appointmentId);
  return ResponseEntity.ok("Appointment cancelled successfully");
 }


 @GetMapping("/available-slots")
 public ResponseEntity<List<LocalTime>> getAvailableSlots(
         @RequestParam Long doctorId,
         @RequestParam LocalDate date) {

  return ResponseEntity.ok(
          appointmentService.getAvailableSlots(doctorId, date)
  );
 }
}
