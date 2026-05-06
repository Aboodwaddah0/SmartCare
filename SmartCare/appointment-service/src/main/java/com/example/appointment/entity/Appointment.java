package com.example.appointment.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(uniqueConstraints = {
		@UniqueConstraint(columnNames = {"doctorId", "date", "time"})
})
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long doctorId;

	private Long patientId;

	private LocalDate date;
	private LocalTime time;

	@Enumerated(EnumType.STRING)
	private AppointmentStatus status;
}
