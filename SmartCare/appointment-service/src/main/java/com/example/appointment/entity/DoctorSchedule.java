package com.example.appointment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
		name = "doctor_schedules",
		uniqueConstraints = @UniqueConstraint(
				columnNames = {"doctor_id", "schedule_date"}
		)
)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "doctor_id", nullable = false)
	private Long doctorId;

	@Column(name = "schedule_date", nullable = false)
	private LocalDate scheduleDate;

	@Column(nullable = false)
	private LocalTime startTime;

	@Column(nullable = false)
	private LocalTime endTime;

	@Column(nullable = false)
	private Integer duration;
}
