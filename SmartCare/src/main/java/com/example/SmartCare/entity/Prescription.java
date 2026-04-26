package com.example.SmartCare.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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

    private List<Medicine> medicines;
    private String notes;

}