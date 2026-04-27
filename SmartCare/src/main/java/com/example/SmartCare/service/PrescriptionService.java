package com.example.SmartCare.service;

import com.example.SmartCare.dto.PrescriptionDto;
import com.example.SmartCare.entity.Appointment;
import com.example.SmartCare.entity.Prescription;
import com.example.SmartCare.exception.ResourceNotFoundException;
import com.example.SmartCare.repository.AppointmentRepository;
import com.example.SmartCare.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
   private final AppointmentRepository appointmentRepository;
    public PrescriptionService(PrescriptionRepository prescriptionRepository, AppointmentRepository appointmentRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public List<PrescriptionDto.PrescriptionResponse> viewPrescription(Long patientId) {
        return prescriptionRepository
                .findByPatientId(patientId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public PrescriptionDto.PrescriptionResponse createPrescription(
            PrescriptionDto.prescriptionRequest request) {

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + request.getAppointmentId()
                ));


        Prescription prescription = Prescription.builder()
                .patientId(appointment.getPatient().getId())
                .doctorId(appointment.getDoctor().getId())
                .appointmentId(request.getAppointmentId())
                .medicines(request.getMedicines())
                .notes(request.getNotes())
                .build();

        Prescription saved = prescriptionRepository.save(prescription);
        return mapToDto(saved);
    }

    public PrescriptionDto.PrescriptionResponse updatePrescription(
            String prescriptionId,
            PrescriptionDto.prescriptionRequest request) {

        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prescription not found with id: " + prescriptionId
                ));

        prescription.setMedicines(request.getMedicines());
        prescription.setNotes(request.getNotes());

        Prescription updated = prescriptionRepository.save(prescription);
        return mapToDto(updated);
    }

    public void deletePrescription(String prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prescription not found with id: " + prescriptionId
                ));

        prescriptionRepository.delete(prescription);
    }



    public List<PrescriptionDto.PrescriptionResponse> getPrescriptionsByPatient(Long patientId){
      return    prescriptionRepository.findByPatientId(patientId).stream()
                .map(this::mapToDto).toList();
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