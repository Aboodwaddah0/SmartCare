package com.example.SmartCare.exception;

import org.springframework.http.HttpStatus;

public class InvalidSlotException extends BusinessException {
    public InvalidSlotException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}