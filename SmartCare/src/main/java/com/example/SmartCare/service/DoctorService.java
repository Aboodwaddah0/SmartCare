package com.example.SmartCare.service;

import com.example.SmartCare.dto.UserDto;
import com.example.SmartCare.entity.Doctor;
import com.example.SmartCare.entity.Role;
import com.example.SmartCare.repository.DoctorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private  final PasswordEncoder passwordEncoder;

    public DoctorService(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createDoctor(UserDto.CreateDoctorRequest request) {

        if (doctorRepository.findByUsername(request.getUsername()).isPresent() ||
                doctorRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Username or Email already exists");
        }
        Doctor doctor = Doctor.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(Role.DOCTOR)
                .specialty(request.getSpecialty())
                .experience(request.getExperience())
                .profilePic(request.getProfilePic())
                .build();

        doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long id){
        if (!doctorRepository.existsById(id)) {
            throw new RuntimeException("Doctor not found");
        }
        doctorRepository.deleteById(id);
    }

    public Doctor getDoctorById(Long id){

        return doctorRepository.findById(id).orElse(null);
    }

    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }

    public Doctor updateDoctor(Long id, Doctor doctor){
        Doctor existingDoctor = doctorRepository.findById(id).orElse(null);
        if (existingDoctor == null) {
            throw new RuntimeException("Doctor not found");
        }
        existingDoctor.setFullName(doctor.getFullName());
        existingDoctor.setEmail(doctor.getEmail());
        existingDoctor.setPhone(doctor.getPhone());
        existingDoctor.setSpecialty(doctor.getSpecialty());
       return   doctorRepository.save(existingDoctor);

    }
    public List<Doctor> getDoctorsBySpecialty(String Specialty){
        return doctorRepository.findBySpecialtyIgnoreCase(Specialty).stream().toList();
    }




}
