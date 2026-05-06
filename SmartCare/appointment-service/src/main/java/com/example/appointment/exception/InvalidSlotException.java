package com.example.appointment.exception;

public class InvalidSlotException extends RuntimeException {
	public InvalidSlotException(String message) {
		super(message);
	}
}
