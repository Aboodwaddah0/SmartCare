package com.example.SmartCare.dto;

import com.example.SmartCare.entity.LabResult;
import com.example.SmartCare.entity.Prescription;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class MedicalHistoryDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class MedicalHistoryRequest {
        private Long appointmentId;
        private LocalDate date;
        private String diagnosis;
        private String notes;
        private List<String> allergies;
        private List<String> chronicDiseases;
        private List<LabResult> labResults;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class MedicalHistoryResponse {
        private String id;
        private Long patientId;
        private Long doctorId;
        private Long appointmentId;
        private LocalDate date;
        private String diagnosis;
        private String notes;
        private List<String> allergies;
        private List<String> chronicDiseases;
        private List<Prescription> prescriptions;
        private List<LabResult> labResults;
    }

}