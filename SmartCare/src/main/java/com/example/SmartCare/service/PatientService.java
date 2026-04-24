package com.example.SmartCare.service;

import com.example.SmartCare.dto.UserDto;
import com.example.SmartCare.entity.Patient;
import com.example.SmartCare.entity.Role;
import com.example.SmartCare.repository.PatientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepository patientRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createPatient(UserDto.CreatePatientRequest request) {
        if (patientRepository.findByUsername(request.getUsername()).isPresent() ||
                patientRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Username or Email already exists");
        }

        Patient patient = Patient.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PATIENT)
                .phone(request.getPhone())
                .gender(request.getGender())
                .build();

        patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not found");
        }
        patientRepository.deleteById(id);
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient updatePatient(Long id, Patient patient) {
        Patient existingPatient = patientRepository.findById(id).orElse(null);
        if (existingPatient == null) {
            throw new RuntimeException("Patient not found");
        }
        existingPatient.setFullName(patient.getFullName());
        existingPatient.setEmail(patient.getEmail());
        existingPatient.setPhone(patient.getPhone());
        existingPatient.setGender(patient.getGender());
        return patientRepository.save(existingPatient);
    }

    public List<Patient> getPatientByName(String name) {
        return patientRepository.findByFullNameContainingIgnoreCase(name).stream().toList();
    }

}