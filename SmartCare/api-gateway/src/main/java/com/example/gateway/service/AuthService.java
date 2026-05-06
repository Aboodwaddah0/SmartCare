package com.example.gateway.service;

import com.example.gateway.config.JwtTokenProvider;
import com.example.gateway.dto.AuthDto;
import com.example.gateway.exception.BadCredentialsException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final JwtTokenProvider jwtTokenProvider;

	@Value("${gateway.admin.username:admin}")
	private String adminUsername;

	@Value("${gateway.admin.password:1234}")
	private String adminPassword;

	public AuthService(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
		if (!adminUsername.equals(request.getUsername()) || !adminPassword.equals(request.getPassword())) {
			throw new BadCredentialsException("Invalid username or password");
		}

		String token = jwtTokenProvider.generateToken(
				request.getUsername(),
				"ADMIN",
				"1"
		);

		return AuthDto.LoginResponse.builder()
				.token(token)
				.username(request.getUsername())
				.role("ADMIN")
				.userId("1")
				.build();
	}

	public Claims validateToken(String token) {
		return jwtTokenProvider.parseToken(token);
	}
}
