package com.example.SmartCare.repository;


import com.example.SmartCare.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
 Optional<Patient> findByUsername(String username);
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByFullNameContainingIgnoreCase(String name);
}
