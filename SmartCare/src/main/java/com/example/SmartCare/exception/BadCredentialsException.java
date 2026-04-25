package com.example.SmartCare.exception;

import org.springframework.http.HttpStatus;

public class BadCredentialsException extends BusinessException {
    public BadCredentialsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
