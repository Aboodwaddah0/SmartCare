package com.example.appointment.exception;

public class SlotAlreadyTakenException extends RuntimeException {
	public SlotAlreadyTakenException(String message) {
		super(message);
	}
}
