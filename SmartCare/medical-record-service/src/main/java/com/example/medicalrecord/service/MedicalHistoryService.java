package com.example.medicalrecord.service;

import com.example.medicalrecord.dto.MedicalHistoryDto;
import com.example.medicalrecord.document.MedicalRecord;
import com.example.medicalrecord.document.Prescription;
import com.example.medicalrecord.exception.ResourceNotFoundException;
import com.example.medicalrecord.repository.MedicalHistoryRepository;
import com.example.medicalrecord.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalHistoryService {

	private final MedicalHistoryRepository medicalHistoryRepository;
	private final PrescriptionRepository prescriptionRepository;

	public MedicalHistoryService(MedicalHistoryRepository medicalHistoryRepository,
								 PrescriptionRepository prescriptionRepository) {
		this.medicalHistoryRepository = medicalHistoryRepository;
		this.prescriptionRepository = prescriptionRepository;
	}

	public List<MedicalHistoryDto.MedicalHistoryResponse> viewMedicalHistory(Long patientId) {
		return medicalHistoryRepository.findByPatientIdOrderByDateDesc(patientId)
				.stream().map(this::mapToDto).toList();
	}

	public MedicalHistoryDto.MedicalHistoryResponse createMedicalHistory(MedicalHistoryDto.MedicalHistoryRequest request) {
		MedicalRecord record = MedicalRecord.builder()
				.patientId(request.getAppointmentId())
				.doctorId(request.getAppointmentId())
				.appointmentId(request.getAppointmentId())
				.date(request.getDate())
				.diagnosis(request.getDiagnosis())
				.notes(request.getNotes())
				.allergies(request.getAllergies())
				.chronicDiseases(request.getChronicDiseases())
				.labResults(request.getLabResults())
				.build();

		MedicalRecord saved = medicalHistoryRepository.save(record);
		return mapToDto(saved);
	}

	public MedicalHistoryDto.MedicalHistoryResponse updateMedicalHistory(String id, MedicalHistoryDto.MedicalHistoryRequest request) {
		MedicalRecord record = medicalHistoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Medical record not found with id: " + id));

		record.setDate(request.getDate());
		record.setDiagnosis(request.getDiagnosis());
		record.setNotes(request.getNotes());
		record.setAllergies(request.getAllergies());
		record.setChronicDiseases(request.getChronicDiseases());
		record.setLabResults(request.getLabResults());

		MedicalRecord updated = medicalHistoryRepository.save(record);
		return mapToDto(updated);
	}

	public void deleteMedicalHistory(String id) {
		MedicalRecord record = medicalHistoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Medical record not found with id: " + id));
		medicalHistoryRepository.delete(record);
	}

	private MedicalHistoryDto.MedicalHistoryResponse mapToDto(MedicalRecord record) {
		List<Prescription> prescriptions = prescriptionRepository.findByPatientId(record.getPatientId());

		return MedicalHistoryDto.MedicalHistoryResponse.builder()
				.id(record.getId())
				.patientId(record.getPatientId())
				.doctorId(record.getDoctorId())
				.appointmentId(record.getAppointmentId())
				.date(record.getDate())
				.diagnosis(record.getDiagnosis())
				.notes(record.getNotes())
				.allergies(record.getAllergies())
				.chronicDiseases(record.getChronicDiseases())
				.prescriptions(prescriptions)
				.labResults(record.getLabResults())
				.build();
	}
}
