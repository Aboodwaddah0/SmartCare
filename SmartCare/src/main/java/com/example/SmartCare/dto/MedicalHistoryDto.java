package com.example.SmartCare.dto;

import com.example.SmartCare.entity.LabResult;
import com.example.SmartCare.entity.Medicine;
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
        private Long doctorId;
        private Long patientId;
        private Long appointmentId;
        private LocalDate date;
        private String diagnosis;
        private String notes;
        private List<String> allergies;
        private List<String> chronicDiseases;
        private List<Medicine> medicines;
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
        private List<Medicine> medicines;
        private List<LabResult> labResults;
    }

}