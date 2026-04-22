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

    private final PatientService patientService;
    private final DoctorService doctorService;

    public AdminService(PatientRepository patientRepository, DoctorRepository doctorRepository, PatientService patientService, DoctorService doctorService, PasswordEncoder passwordEncoder) {
        this.patientService = patientService;
        this.doctorService = doctorService;

    }

    public void createPatient(UserDto.CreatePatientRequest request){
         patientService.createPatient(request);
    }

    public void createDoctor(UserDto.CreateDoctorRequest request){
       doctorService.createDoctor(request);
    }

    public void deletePatient(Long id){
      patientService.deletePatient(id);
    }
    public void deleteDoctor(Long id){
        doctorService.deleteDoctor(id);
    }

    public Patient getPatientById(Long id){

        return patientService.getPatientById(id);
    }

    public Doctor getDoctorById(Long id){

        return doctorService.getDoctorById(id);
    }

    public List<Patient> getAllPatients(){
        return  patientService.getAllPatients();
    }

    public List<Doctor> getAllDoctors(){

        return doctorService.getAllDoctors();
    }

}

