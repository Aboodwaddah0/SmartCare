package com.example.SmartCare.service;

import com.example.SmartCare.dto.MedicalHistoryDto;
import com.example.SmartCare.entity.Appointment;
import com.example.SmartCare.entity.MedicalRecord;
import com.example.SmartCare.entity.Prescription;
import com.example.SmartCare.exception.ResourceNotFoundException;
import com.example.SmartCare.repository.AppointmentRepository;
import com.example.SmartCare.repository.MedicalHistoryRepository;
import com.example.SmartCare.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalHistoryService {

    private final MedicalHistoryRepository medicalHistoryRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    public MedicalHistoryService(MedicalHistoryRepository medicalHistoryRepository,
                                 AppointmentRepository appointmentRepository, PrescriptionRepository prescriptionRepository) {
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    public List<MedicalHistoryDto.MedicalHistoryResponse> viewMedicalHistory(Long patientId) {
        return medicalHistoryRepository
                .findByPatientIdOrderByDateDesc(patientId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public MedicalHistoryDto.MedicalHistoryResponse createMedicalHistory(
            MedicalHistoryDto.MedicalHistoryRequest request) {

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + request.getAppointmentId()
                ));

        MedicalRecord record = MedicalRecord.builder()
                .doctorId(appointment.getDoctor().getId())
                .patientId(appointment.getPatient().getId())
                .appointmentId(appointment.getId())
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

    public MedicalHistoryDto.MedicalHistoryResponse updateMedicalHistory(
            String id,
            MedicalHistoryDto.MedicalHistoryRequest request) {

        MedicalRecord record = medicalHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medical record not found with id: " + id
                ));

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
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medical record not found with id: " + id
                ));

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