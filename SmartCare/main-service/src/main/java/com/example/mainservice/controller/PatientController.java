package com.example.mainservice.controller;

import com.example.mainservice.dto.UserDto;
import com.example.mainservice.dto.UserDto.PatientResponse;
import com.example.mainservice.dto.ApiResponse;
import com.example.mainservice.entity.Patient;
import com.example.mainservice.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/patients")
public class PatientController {

	private final PatientService patientService;

	public PatientController(PatientService patientService) {
		this.patientService = patientService;
	}

	@PostMapping
	public ResponseEntity<ApiResponse> createPatient(@RequestBody UserDto.CreatePatientRequest request) {
		patientService.createPatient(request);
		return ResponseEntity.ok(ApiResponse.success("Patient created successfully"));
	}

	@GetMapping("/{id}")
	public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
		Patient patient = patientService.getPatientById(id);
		if (patient == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(mapToPatientResponse(patient));
	}

	@GetMapping
	public ResponseEntity<List<PatientResponse>> getAllPatients() {
		List<PatientResponse> responses = patientService.getAllPatients().stream()
				.map(this::mapToPatientResponse)
				.toList();
		return ResponseEntity.ok(responses);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deletePatient(@PathVariable Long id) {
		patientService.deletePatient(id);
		return ResponseEntity.ok(ApiResponse.success("Patient deleted successfully"));
	}

	@PutMapping("/{id}")
	public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
		Patient updatedPatient = patientService.updatePatient(id, patient);
		return ResponseEntity.ok(mapToPatientResponse(updatedPatient));
	}

	@GetMapping("/search/{username}")
	public ResponseEntity<List<PatientResponse>> searchPatientByUsername(@PathVariable String username) {
		List<Patient> patients = patientService.getPatientByName(username);
		List<PatientResponse> patientResponses = patients.stream()
				.map(this::mapToPatientResponse)
				.toList();
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
