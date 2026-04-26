package com.example.SmartCare.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "medical_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {

    @Id
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