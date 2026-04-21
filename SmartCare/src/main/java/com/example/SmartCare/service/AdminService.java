package com.example.SmartCare.service;

import com.example.SmartCare.dto.UserDto;
import com.example.SmartCare.entity.Doctor;
import com.example.SmartCare.entity.Patient;
import com.example.SmartCare.entity.Role;
import com.example.SmartCare.entity.User;
import com.example.SmartCare.repository.DoctorRepository;
import com.example.SmartCare.repository.PatientRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final PatientRepository patientRepository ;
    private final DoctorRepository doctorRepository;
  private  final PasswordEncoder passwordEncoder;
    public AdminService(PatientRepository patientRepository, DoctorRepository doctorRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createPatient(UserDto.CreatePatientRequest request){

        Patient patient = Patient.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PATIENT)
                .phone(request.getPhone())
                .age(request.getAge())
                .gender(request.getGender())
                .build();

        patientRepository.save(patient);
    }

    public void createDoctor(UserDto.CreateDoctorRequest request){
        Doctor doctor = Doctor.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.DOCTOR)
                .phone(request.getPhone())
                .specialization(request.getSpecialization())
                .build();

        doctorRepository.save(doctor);
    }

    public void deletePatient(Long id){
        patientRepository.deleteById(id);
    }
    public void deleteDoctor(Long id){
        doctorRepository.deleteById(id);
    }

    public Patient getPatientById(Long id){
        return patientRepository.findById(id).orElse(null);
    }

    public Doctor getDoctorById(Long id){
        return doctorRepository.findById(id).orElse(null);
    }

    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }
    public List<Doctor> getAllDoctors(){
        return doctorRepository.findAll();
    }

}
