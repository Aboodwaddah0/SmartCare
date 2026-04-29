package com.example.SmartCare.service;

import com.example.SmartCare.dto.UserDto;
import com.example.SmartCare.entity.Doctor;
import com.example.SmartCare.entity.Role;
import com.example.SmartCare.entity.User;
import com.example.SmartCare.exception.ResourceAlreadyExistsException;
import com.example.SmartCare.exception.ResourceNotFoundException;
import com.example.SmartCare.repository.DoctorRepository;
import com.example.SmartCare.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createDoctor(UserDto.CreateDoctorRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent() ||
                userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("Username or Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(Role.DOCTOR)
                .build();

        User savedUser = userRepository.save(user);

        Doctor doctor = Doctor.builder()
                .user(savedUser)
                .specialty(request.getSpecialty())
                .experience(request.getExperience())
                .profilePic(request.getProfilePic())
                .build();

        doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id){
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        userRepository.delete(doctor.getUser());
        doctorRepository.delete(doctor);
    }

    public Doctor getDoctorById(Long id){
        return doctorRepository.findById(id).orElse(null);
    }





    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }

    public Doctor updateDoctor(Long id, Doctor doctorUpdate){
        Doctor existingDoctor = doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        User user = existingDoctor.getUser();
        user.setFullName(doctorUpdate.getUser().getFullName());
        user.setEmail(doctorUpdate.getUser().getEmail());
        user.setPhone(doctorUpdate.getUser().getPhone());
        userRepository.save(user);

        existingDoctor.setSpecialty(doctorUpdate.getSpecialty());
        existingDoctor.setExperience(doctorUpdate.getExperience());
        existingDoctor.setProfilePic(doctorUpdate.getProfilePic());
        return doctorRepository.save(existingDoctor);
    }

    public List<Doctor> getDoctorsBySpecialty(String specialty){
        return doctorRepository.findBySpecialtyIgnoreCase(specialty).stream().toList();
    }



}