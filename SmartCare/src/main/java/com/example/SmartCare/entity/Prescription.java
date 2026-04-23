package com.example.SmartCare.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import lombok.*;
@Document(collection = "prescriptions")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {

    @Id
    private String id;

    private Long patientId;
    private Long doctorId;
    private Long appointmentId;

    private LocalDate date;

    private List<Medicine> medicines;
    private String notes;
    private List<LabResult> labResults;




}

