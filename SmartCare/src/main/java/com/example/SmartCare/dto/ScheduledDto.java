package com.example.SmartCare.dto;

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
     LocalDate scheduleDate;
     LocalTime startTime;
     LocalTime endTime;
     int slotDuration;
 }
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class ScheduleResponse {
        Long id;
        LocalDate scheduleDate;
        LocalTime startTime;
        LocalTime endTime;
        List<LocalTime> Slots;
    }

}
