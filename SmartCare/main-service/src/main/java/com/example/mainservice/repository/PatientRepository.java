package com.example.mainservice.repository;

import com.example.mainservice.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
	List<Patient> findByUser_FullNameContainingIgnoreCase(String fullName);
}
