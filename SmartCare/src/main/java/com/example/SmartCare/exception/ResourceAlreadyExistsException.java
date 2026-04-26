package com.example.SmartCare.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends BusinessException {
    public ResourceAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}