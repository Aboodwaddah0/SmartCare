package com.example.medicalrecord.document;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

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
	private Long doctorId;
	private Long appointmentId;
	private List<Medicine> medicines;
	private String notes;
}
