package com.example.medicalrecord.document;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine {
	private String name;
	private String dosage;
	private String frequency;
	private Integer durationDays;
	private String instructions;
}
