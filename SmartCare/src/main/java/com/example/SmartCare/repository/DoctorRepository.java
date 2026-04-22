package com.example.SmartCare.repository;


import com.example.SmartCare.entity.Doctor;
import com.example.SmartCare.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository  extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUsername(String username);
    Optional<Doctor> findByEmail(String email);
    Optional<Doctor> findBySpecializationIgnoreCase(String Specialization);
}
