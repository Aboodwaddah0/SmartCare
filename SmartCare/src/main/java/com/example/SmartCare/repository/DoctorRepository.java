package com.example.SmartCare.repository;


import com.example.SmartCare.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository  extends JpaRepository<Doctor, Long> {
}
