package com.example.gateway.controller;

import com.example.gateway.dto.AuthDto;
import com.example.gateway.dto.ApiResponse;
import com.example.gateway.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@RequestBody AuthDto.LoginRequest request) {
		return ResponseEntity.ok(ApiResponse.success(authService.login(request)));
	}
}
