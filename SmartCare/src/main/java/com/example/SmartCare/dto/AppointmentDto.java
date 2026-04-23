package com.example.SmartCare.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentDto {


    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class BookingRequest  {

        private Long doctorId;
        private Long patientId;
        private LocalDate date;
        private LocalTime time;

    }

}
