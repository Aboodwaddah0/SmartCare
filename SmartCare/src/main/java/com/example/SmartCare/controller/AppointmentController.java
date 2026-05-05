package com.example.SmartCare.controller;


import com.example.SmartCare.dto.AppointmentDto;
import com.example.SmartCare.dto.PrescriptionDto;
import com.example.SmartCare.dto.ApiResponse;
import com.example.SmartCare.entity.Appointment;
import com.example.SmartCare.entity.Prescription;
import com.example.SmartCare.repository.AppointmentRepository;
import com.example.SmartCare.service.AppointmentService;
import com.example.SmartCare.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

 private final AppointmentService appointmentService;

 public AppointmentController(AppointmentService appointmentService, AppointmentRepository appointmentRepository, PrescriptionService prescriptionService) {
  this.appointmentService = appointmentService;


 }


    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDto.AppointmentResponse>> getAppointmentsDoctor(
            @PathVariable Long doctorId,
            @RequestParam LocalDate date) {

        return ResponseEntity.ok(
                appointmentService.getAllAppointments(doctorId, date)
        );
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDto.AppointmentResponse>> getPatientAppointments(
            @PathVariable Long patientId) {
        return ResponseEntity.ok(
                appointmentService.getPatientAppointments(patientId)
        );
    }

   @PostMapping("")
  public ResponseEntity<ApiResponse> bookAppointment(
          @RequestBody AppointmentDto.BookingRequest request) {
   appointmentService.bookAppointment(request);
   return ResponseEntity.ok(ApiResponse.success("Appointment booked successfully"));
  }

    @PatchMapping ("/{appointmentId}")
   public ResponseEntity<ApiResponse> cancelAppointment(
           @PathVariable Long appointmentId) {

    appointmentService.cancel(appointmentId);
    return ResponseEntity.ok(ApiResponse.success("Appointment cancelled successfully"));
   }

    @PatchMapping ("/{appointmentId}/complete")
   public ResponseEntity<ApiResponse> completeAppointment(
           @PathVariable Long appointmentId) {

    appointmentService.complete(appointmentId);
    return ResponseEntity.ok(ApiResponse.success("Appointment completed successfully"));
   }

  @GetMapping("/available-slots/{doctorId}")
 public ResponseEntity<List<LocalTime>> getAvailableSlots(
         @PathVariable Long doctorId,
         @RequestParam LocalDate date) {

  return ResponseEntity.ok(appointmentService.getAvailableSlots(doctorId, date)
  );
 }



}
