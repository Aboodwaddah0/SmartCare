package com.example.SmartCare.controller;

import com.example.SmartCare.dto.ApiResponse;
import com.example.SmartCare.dto.MedicalHistoryDto;
import com.example.SmartCare.service.MedicalHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/medical-history")
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;

    public MedicalHistoryController(MedicalHistoryService medicalHistoryService) {
        this.medicalHistoryService = medicalHistoryService;
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping
    public ResponseEntity<MedicalHistoryDto.MedicalHistoryResponse> create(
            @RequestBody MedicalHistoryDto.MedicalHistoryRequest request) {
        return ResponseEntity.ok(
                medicalHistoryService.createMedicalHistory(request)
        );
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<MedicalHistoryDto.MedicalHistoryResponse> update(
            @PathVariable String id,
            @RequestBody MedicalHistoryDto.MedicalHistoryRequest request) {
        return ResponseEntity.ok(
                medicalHistoryService.updateMedicalHistory(id, request)
        );
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String id) {
        medicalHistoryService.deleteMedicalHistory(id);
        return ResponseEntity.ok(
                ApiResponse.success("Medical history deleted successfully")
        );
    }

    @PreAuthorize("hasAnyRole('PATIENT','DOCTOR','ADMIN')")
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPatientMedicalHistory(@PathVariable Long patientId) {
        return ResponseEntity.ok(
                medicalHistoryService.viewMedicalHistory(patientId)
        );
    }
}