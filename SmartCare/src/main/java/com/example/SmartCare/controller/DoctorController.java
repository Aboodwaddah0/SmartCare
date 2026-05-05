package com.example.SmartCare.controller;

import com.example.SmartCare.dto.UserDto;
import com.example.SmartCare.dto.UserDto.DoctorResponse;
import com.example.SmartCare.dto.ApiResponse;
import com.example.SmartCare.entity.Doctor;
import com.example.SmartCare.service.DoctorService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createDoctor(@RequestBody UserDto.CreateDoctorRequest request) {
        doctorService.createDoctor(request);
        return ResponseEntity.ok(ApiResponse.success("Doctor created successfully"));
    }
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapToDoctorResponse(doctor));
    }

    @GetMapping
    @Cacheable("doctors")
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        List<DoctorResponse> responses = doctorService.getAllDoctors().stream()
                .map(this::mapToDoctorResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor deleted successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        Doctor updatedDoctor = doctorService.updateDoctor(id, doctor);
        return ResponseEntity.ok(mapToDoctorResponse(updatedDoctor));
    }

    @GetMapping("/specialty")
    public ResponseEntity<List<DoctorResponse>> getDoctorsBySpecialty(@RequestParam String specialty) {
        List<Doctor> doctors = doctorService.getDoctorsBySpecialty(specialty);
        List<DoctorResponse> responses = doctors.stream()
                .map(this::mapToDoctorResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    private DoctorResponse mapToDoctorResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .fullName(doctor.getUser().getFullName())
                .username(doctor.getUser().getUsername())
                .email(doctor.getUser().getEmail())
                .phone(doctor.getUser().getPhone())
                .specialty(doctor.getSpecialty())
                .experience(doctor.getExperience())
                .profilePic(doctor.getProfilePic())
                .role(doctor.getUser().getRole())
                .build();
    }


}
