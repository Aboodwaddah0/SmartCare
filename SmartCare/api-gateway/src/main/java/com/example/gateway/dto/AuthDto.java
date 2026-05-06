package com.example.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthDto {

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Getter
	public static class LoginRequest {
		private String username;
		private String password;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Getter
	public static class LoginResponse {
		private String token;
		private String username;
		private String role;
		private String userId;
	}
}
