package com.example.medicalrecord.repository;

import com.example.medicalrecord.document.MedicalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MedicalHistoryRepository extends MongoRepository<MedicalRecord, String> {
	List<MedicalRecord> findByPatientIdOrderByDateDesc(Long patientId);
}
