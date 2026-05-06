package com.example.appointment.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ScheduledDto {

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Getter
	@Setter
	public static class ScheduleRequest {
		private LocalDate scheduleDate;
		private LocalTime startTime;
		private LocalTime endTime;
		private Integer slotDuration;
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Getter
	@Setter
	public static class ScheduleResponse {
		private Long id;
		private Long doctorId;
		private LocalDate scheduleDate;
		private LocalTime startTime;
		private LocalTime endTime;
		private List<LocalTime> Slots;
	}
}
