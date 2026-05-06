package com.example.medicalrecord.dto;

import com.example.medicalrecord.document.Medicine;
import lombok.*;

import java.util.List;

public class PrescriptionDto {

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Getter
	@Setter
	public static class PrescriptionRequest {
		private Long appointmentId;
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
		private Long doctorId;
		private Long appointmentId;
		private List<Medicine> medicines;
		private String notes;
	}
}
