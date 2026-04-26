package com.example.SmartCare.dto;

import com.example.SmartCare.entity.Medicine;
import lombok.*;

import java.util.List;

public class PrescriptionDto {
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class prescriptionRequest {
        private Long patientId;
        private List<Medicine> medicines;
        private String notes;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class PrescriptionResponse {
        private String id;
        private Long patientId;
        private List<Medicine> medicines;
        private String notes;
    }

}