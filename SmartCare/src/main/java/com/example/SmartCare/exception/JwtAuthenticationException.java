package com.example.SmartCare.exception;

import org.springframework.http.HttpStatus;

public class JwtAuthenticationException extends BusinessException {

    public JwtAuthenticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public JwtAuthenticationException(String message, HttpStatus status) {
        super(message, status);
    }
}