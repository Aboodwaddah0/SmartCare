package com.example.SmartCare.repository;

import com.example.SmartCare.entity.MedicalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalHistoryRepository extends MongoRepository<MedicalRecord, String> {
    List<MedicalRecord> findByPatientIdOrderByDateDesc(Long patientId);
    List<MedicalRecord> findByDoctorId(Long doctorId);
}