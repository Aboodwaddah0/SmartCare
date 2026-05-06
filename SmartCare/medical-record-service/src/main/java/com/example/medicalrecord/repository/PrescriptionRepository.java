package com.example.medicalrecord.repository;

import com.example.medicalrecord.document.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
	List<Prescription> findByPatientId(Long patientId);
}
