package com.example.SmartCare.repository;


import com.example.SmartCare.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("SELECT p FROM Patient p WHERE p.user.username = :username")
    Optional<Patient> findByUsername(String username);

    @Query("SELECT p FROM Patient p WHERE p.user.email = :email")
    Optional<Patient> findByEmail(String email);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.user.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Patient> findByFullNameContainingIgnoreCase(String name);

    @Query("SELECT p FROM Patient p WHERE p.user.id = :userId")
    Optional<Patient> findByUserId(Long userId);
}