package com.example.medicalrecord.document;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabResult {
	private String testName;
	private String result;
	private String unit;
	private String referenceRange;
	private LocalDate date;
}
