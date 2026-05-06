package com.example.mainservice.repository;

import com.example.mainservice.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
	List<Doctor> findBySpecialtyIgnoreCase(String specialty);
}
