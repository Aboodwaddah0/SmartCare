package com.example.appointment.exception;

public class NoScheduleException extends RuntimeException {
	public NoScheduleException(String message) {
		super(message);
	}
}
