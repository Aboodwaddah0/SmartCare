package com.example.appointment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
				"status", 404, "error", "Not Found", "message", ex.getMessage()));
	}

	@ExceptionHandler(SlotAlreadyTakenException.class)
	public ResponseEntity<Map<String, Object>> handleSlotTaken(SlotAlreadyTakenException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
				"status", 409, "error", "Conflict", "message", ex.getMessage()));
	}

	@ExceptionHandler(InvalidSlotException.class)
	public ResponseEntity<Map<String, Object>> handleInvalidSlot(InvalidSlotException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
				"status", 400, "error", "Bad Request", "message", ex.getMessage()));
	}

	@ExceptionHandler(NoScheduleException.class)
	public ResponseEntity<Map<String, Object>> handleNoSchedule(NoScheduleException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
				"status", 404, "error", "Not Found", "message", ex.getMessage()));
	}

	@ExceptionHandler(InvalidOperationException.class)
	public ResponseEntity<Map<String, Object>> handleInvalidOp(InvalidOperationException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
				"status", 400, "error", "Bad Request", "message", ex.getMessage()));
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<Map<String, Object>> handleBusiness(BusinessException ex) {
		return ResponseEntity.status(ex.getStatus()).body(Map.of(
				"status", ex.getStatus().value(), "error", ex.getStatus().getReasonPhrase(), "message", ex.getMessage()));
	}
}
