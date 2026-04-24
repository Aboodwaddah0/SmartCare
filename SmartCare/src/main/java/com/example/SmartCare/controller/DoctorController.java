package com.example.SmartCare.controller;

import com.example.SmartCare.dto.UserDto;
import com.example.SmartCare.dto.UserDto.DoctorResponse;
import com.example.SmartCare.entity.Doctor;
import com.example.SmartCare.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createDoctor(@RequestBody UserDto.CreateDoctorRequest request) {
        doctorService.createDoctor(request);
        return ResponseEntity.ok("Doctor created successfully");
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR') or hasRole('ROLE_PATIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapToDoctorResponse(doctor));
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        List<DoctorResponse> responses = doctorService.getAllDoctors().stream()
                .map(this::mapToDoctorResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok("Doctor deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        Doctor updatedDoctor = doctorService.updateDoctor(id, doctor);
        return ResponseEntity.ok(mapToDoctorResponse(updatedDoctor));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PATIENT') ")
    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<List<DoctorResponse>> getDoctorsBySpecialty(@PathVariable String specialty) {
        List<Doctor> doctors = doctorService.getDoctorsBySpecialty(specialty);
        List<DoctorResponse> responses = doctors.stream()
                .map(this::mapToDoctorResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    private DoctorResponse mapToDoctorResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .username(doctor.getUsername())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .specialty(doctor.getSpecialty())
                .experience(doctor.getExperience())
                .profilePic(doctor.getProfilePic())
                .role(doctor.getRole())
                .build();
    }


}
