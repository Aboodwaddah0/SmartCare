package com.example.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ApiResponse {
	private int status;
	private String message;
	private Object data;

	public static ApiResponse success(String message) {
		return ApiResponse.builder()
				.status(200)
				.message(message)
				.build();
	}

	public static ApiResponse success(Object data) {
		return ApiResponse.builder()
				.status(200)
				.message("success")
				.data(data)
				.build();
	}
}
