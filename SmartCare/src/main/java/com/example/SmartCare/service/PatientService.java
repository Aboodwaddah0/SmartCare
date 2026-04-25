package com.example.SmartCare.service;

import com.example.SmartCare.dto.UserDto;
import com.example.SmartCare.entity.Patient;
import com.example.SmartCare.entity.Role;
import com.example.SmartCare.entity.User;
import com.example.SmartCare.exception.ResourceAlreadyExistsException;
import com.example.SmartCare.exception.ResourceNotFoundException;
import com.example.SmartCare.repository.PatientRepository;
import com.example.SmartCare.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepository patientRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createPatient(UserDto.CreatePatientRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent() ||
                userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Username or Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PATIENT)
                .phone(request.getPhone())
                .build();

        User savedUser = userRepository.save(user);

        Patient patient = Patient.builder()
                .user(savedUser)
                .gender(request.getGender())
                .bloodType(request.getBloodType())
                .address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth())
                .build();

        patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        userRepository.delete(patient.getUser());
        patientRepository.delete(patient);
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient updatePatient(Long id, Patient patientUpdate) {
        Patient existingPatient = patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        User user = existingPatient.getUser();
        user.setFullName(patientUpdate.getUser().getFullName());
        user.setEmail(patientUpdate.getUser().getEmail());
        user.setPhone(patientUpdate.getUser().getPhone());
        userRepository.save(user);

        existingPatient.setGender(patientUpdate.getGender());
        return patientRepository.save(existingPatient);
    }

    public List<Patient> getPatientByName(String name) {
        return patientRepository.findByFullNameContainingIgnoreCase(name).stream().toList();
    }

}