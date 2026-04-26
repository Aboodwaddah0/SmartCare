package com.example.SmartCare.exception;

import org.springframework.http.HttpStatus;

public class SlotAlreadyTakenException extends BusinessException {
    public SlotAlreadyTakenException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}