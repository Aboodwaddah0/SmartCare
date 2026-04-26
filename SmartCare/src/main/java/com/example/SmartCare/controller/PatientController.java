package com.example.SmartCare.controller;

import com.example.SmartCare.dto.UserDto;
import com.example.SmartCare.dto.UserDto.PatientResponse;
import com.example.SmartCare.dto.ApiResponse;
import com.example.SmartCare.entity.Patient;
import com.example.SmartCare.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

@PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createPatient(@RequestBody UserDto.CreatePatientRequest request) {
        patientService.createPatient(request);
        return ResponseEntity.ok(ApiResponse.success("Patient created successfully"));
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT')")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        if (patient == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapToPatientResponse(patient));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR')")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        List<PatientResponse> responses = patientService.getAllPatients().stream()
                .map(this::mapToPatientResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(ApiResponse.success("Patient deleted successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT')")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        Patient updatedPatient = patientService.updatePatient(id, patient);
        return ResponseEntity.ok(mapToPatientResponse(updatedPatient));
    }

    @GetMapping("/search/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') or hasRole('PATIENT')")
    public ResponseEntity<List<PatientResponse>> searchPatientByUsername(@PathVariable String username) {
        List<Patient> patient = patientService.getPatientByName(username);
       List <PatientResponse> patientResponses=patient.stream().map(this::mapToPatientResponse).toList();
       return ResponseEntity.ok(patientResponses);
    }

    private PatientResponse mapToPatientResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .fullName(patient.getUser().getFullName())
                .username(patient.getUser().getUsername())
                .email(patient.getUser().getEmail())
                .phone(patient.getUser().getPhone())
                .dateOfBirth(patient.getDateOfBirth() != null ? patient.getDateOfBirth().toString() : null)
                .gender(patient.getGender())
                .role(patient.getUser().getRole())
                .build();
    }



}