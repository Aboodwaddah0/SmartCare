package com.example.medicalrecord.controller;

import com.example.medicalrecord.dto.ApiResponse;
import com.example.medicalrecord.dto.MedicalHistoryDto;
import com.example.medicalrecord.service.MedicalHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/medical-history")
public class MedicalHistoryController {

	private final MedicalHistoryService medicalHistoryService;

	public MedicalHistoryController(MedicalHistoryService medicalHistoryService) {
		this.medicalHistoryService = medicalHistoryService;
	}

	@PostMapping
	public ResponseEntity<MedicalHistoryDto.MedicalHistoryResponse> create(
			@RequestBody MedicalHistoryDto.MedicalHistoryRequest request) {
		return ResponseEntity.ok(medicalHistoryService.createMedicalHistory(request));
	}

	@PutMapping("/{id}")
	public ResponseEntity<MedicalHistoryDto.MedicalHistoryResponse> update(
			@PathVariable String id,
			@RequestBody MedicalHistoryDto.MedicalHistoryRequest request) {
		return ResponseEntity.ok(medicalHistoryService.updateMedicalHistory(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> delete(@PathVariable String id) {
		medicalHistoryService.deleteMedicalHistory(id);
		return ResponseEntity.ok(ApiResponse.success("Medical history deleted successfully"));
	}



	@GetMapping("/patient/{patientId}")
	public ResponseEntity<?> getPatientMedicalHistory(@PathVariable Long patientId) {
		return ResponseEntity.ok(medicalHistoryService.viewMedicalHistory(patientId));
	}
}
