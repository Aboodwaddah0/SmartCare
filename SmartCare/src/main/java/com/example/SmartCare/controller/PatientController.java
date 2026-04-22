package com.example.SmartCare.controller;

import com.example.SmartCare.dto.UserDto;
import com.example.SmartCare.dto.UserDto.PatientResponse;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createPatient(@RequestBody UserDto.CreatePatientRequest request) {
        patientService.createPatient(request);
        return ResponseEntity.ok("Patient created successfully");
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR') or hasRole('ROLE_PATIENT')")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        if (patient == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapToPatientResponse(patient));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR')")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        List<PatientResponse> responses = patientService.getAllPatients().stream()
                .map(this::mapToPatientResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok("Patient deleted successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PATIENT')")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        Patient updatedPatient = patientService.updatePatient(id, patient);
        return ResponseEntity.ok(mapToPatientResponse(updatedPatient));
    }

    @GetMapping("/search/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR') or hasRole('ROLE_PATIENT')")
    public ResponseEntity<List<PatientResponse>> searchPatientByUsername(@PathVariable String username) {
        List<Patient> patient = patientService.getPatientByName(username);
       List <PatientResponse> patientResponses=patient.stream().map(this::mapToPatientResponse).toList();
       return ResponseEntity.ok(patientResponses);
    }

    private PatientResponse mapToPatientResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .username(patient.getUsername())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .age(patient.getAge())
                .gender(patient.getGender())
                .role(patient.getRole())
                .build();
    }



}