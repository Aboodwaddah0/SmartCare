package com.example.SmartCare.dto;

public class ApiResponse {
    private String message;

    public ApiResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ApiResponse success(String message) {
        return new ApiResponse(message);
    }
}