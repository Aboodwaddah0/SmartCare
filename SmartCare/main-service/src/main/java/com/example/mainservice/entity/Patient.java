package com.example.mainservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "patient")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Patient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private LocalDate dateOfBirth;

	private String address;

	@Column(length = 5)
	private String bloodType;

	private String gender;
}
