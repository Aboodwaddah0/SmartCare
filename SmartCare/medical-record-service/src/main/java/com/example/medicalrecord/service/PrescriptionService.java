package com.example.medicalrecord.service;

import com.example.medicalrecord.dto.PrescriptionDto;
import com.example.medicalrecord.document.Prescription;
import com.example.medicalrecord.exception.ResourceNotFoundException;
import com.example.medicalrecord.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {

	private final PrescriptionRepository prescriptionRepository;

	public PrescriptionService(PrescriptionRepository prescriptionRepository) {
		this.prescriptionRepository = prescriptionRepository;
	}

	public List<PrescriptionDto.PrescriptionResponse> viewPrescriptions(Long patientId) {
		return prescriptionRepository.findByPatientId(patientId)
				.stream().map(this::mapToDto).toList();
	}

	public PrescriptionDto.PrescriptionResponse createPrescription(PrescriptionDto.PrescriptionRequest request) {
		Prescription prescription = Prescription.builder()
				.patientId(request.getAppointmentId())
				.doctorId(request.getAppointmentId())
				.appointmentId(request.getAppointmentId())
				.medicines(request.getMedicines())
				.notes(request.getNotes())
				.build();

		Prescription saved = prescriptionRepository.save(prescription);
		return mapToDto(saved);
	}

	public PrescriptionDto.PrescriptionResponse updatePrescription(String prescriptionId, PrescriptionDto.PrescriptionRequest request) {
		Prescription prescription = prescriptionRepository.findById(prescriptionId)
				.orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + prescriptionId));

		prescription.setMedicines(request.getMedicines());
		prescription.setNotes(request.getNotes());

		Prescription updated = prescriptionRepository.save(prescription);
		return mapToDto(updated);
	}

	public void deletePrescription(String prescriptionId) {
		Prescription prescription = prescriptionRepository.findById(prescriptionId)
				.orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + prescriptionId));
		prescriptionRepository.delete(prescription);
	}

	private PrescriptionDto.PrescriptionResponse mapToDto(Prescription p) {
		return PrescriptionDto.PrescriptionResponse.builder()
				.id(p.getId())
				.patientId(p.getPatientId())
				.doctorId(p.getDoctorId())
				.appointmentId(p.getAppointmentId())
				.medicines(p.getMedicines())
				.notes(p.getNotes())
				.build();
	}
}
