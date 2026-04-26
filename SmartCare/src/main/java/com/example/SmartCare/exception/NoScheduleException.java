package com.example.SmartCare.exception;

import org.springframework.http.HttpStatus;

public class NoScheduleException extends BusinessException {
    public NoScheduleException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}