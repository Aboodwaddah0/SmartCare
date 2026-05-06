package com.example.medicalrecord.controller;

import com.example.medicalrecord.dto.ApiResponse;
import com.example.medicalrecord.dto.PrescriptionDto;
import com.example.medicalrecord.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/prescriptions")
public class PrescriptionController {

	private final PrescriptionService prescriptionService;

	public PrescriptionController(PrescriptionService prescriptionService) {
		this.prescriptionService = prescriptionService;
	}

	@PostMapping
	public ResponseEntity<PrescriptionDto.PrescriptionResponse> create(
			@RequestBody PrescriptionDto.PrescriptionRequest request) {
		return ResponseEntity.ok(prescriptionService.createPrescription(request));
	}

	@PutMapping("/{prescriptionId}")
	public ResponseEntity<PrescriptionDto.PrescriptionResponse> update(
			@PathVariable String prescriptionId,
			@RequestBody PrescriptionDto.PrescriptionRequest request) {
		return ResponseEntity.ok(prescriptionService.updatePrescription(prescriptionId, request));
	}

	@DeleteMapping("/{prescriptionId}")
	public ResponseEntity<ApiResponse> delete(@PathVariable String prescriptionId) {
		prescriptionService.deletePrescription(prescriptionId);
		return ResponseEntity.ok(ApiResponse.success("Prescription deleted successfully"));
	}

	@GetMapping("/patient/{patientId}")
	public ResponseEntity<?> getPatientPrescriptions(@PathVariable Long patientId) {
		return ResponseEntity.ok(prescriptionService.viewPrescriptions(patientId));
	}
}
