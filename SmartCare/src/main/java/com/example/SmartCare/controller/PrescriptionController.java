package com.example.SmartCare.controller;

import com.example.SmartCare.dto.ApiResponse;
import com.example.SmartCare.dto.PrescriptionDto;
import com.example.SmartCare.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prescriptions")
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }


    @PostMapping
    public ResponseEntity<PrescriptionDto.PrescriptionResponse> create(
            @RequestBody PrescriptionDto.prescriptionRequest request) {

        return ResponseEntity.ok(
                prescriptionService.createPrescription(request)
        );
    }


    @PutMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionDto.PrescriptionResponse> update(
            @PathVariable String prescriptionId,
            @RequestBody PrescriptionDto.prescriptionRequest request) {

        return ResponseEntity.ok(
                prescriptionService.updatePrescription(prescriptionId, request)
        );
    }


    @DeleteMapping("/{prescriptionId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String prescriptionId) {

        prescriptionService.deletePrescription(prescriptionId);

        return ResponseEntity.ok(
                ApiResponse.success("Prescription deleted successfully")
        );
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<?> getPatientPrescriptions(@PathVariable Long patientId) {

        return ResponseEntity.ok(
                prescriptionService.viewPrescription(patientId)
        );
    }
}