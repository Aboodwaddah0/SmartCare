package com.example.mainservice.dto;

import com.example.mainservice.entity.Role;
import lombok.*;

public class UserDto {

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Getter
	@Setter
	public static class CreateDoctorRequest {
		private String fullName;
		private String username;
		private String password;
		private String email;
		private String phone;
		private String specialty;
		private String experience;
		private String profilePic;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Getter
	@Setter
	public static class CreatePatientRequest {
		private String fullName;
		private String username;
		private String password;
		private String email;
		private String phone;
		private String gender;
		private String bloodType;
		private String address;
		private String dateOfBirth;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Getter
	@Setter
	public static class DoctorResponse {
		private Long id;
		private String fullName;
		private String username;
		private String email;
		private String phone;
		private String specialty;
		private String experience;
		private String profilePic;
		private Role role;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Getter
	@Setter
	public static class PatientResponse {
		private Long id;
		private String fullName;
		private String username;
		private String email;
		private String phone;
		private String dateOfBirth;
		private String gender;
		private Role role;
	}
}
