package com.example.SmartCare.service;

import com.example.SmartCare.dto.ScheduledDto;
import com.example.SmartCare.entity.Doctor;
import com.example.SmartCare.entity.DoctorSchedule;
import com.example.SmartCare.exception.InvalidOperationException;
import com.example.SmartCare.exception.ResourceNotFoundException;
import com.example.SmartCare.repository.DoctorRepository;
import com.example.SmartCare.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class DoctorScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;

    public DoctorScheduleService(ScheduleRepository scheduleRepository,
                                 DoctorRepository doctorRepository) {
        this.scheduleRepository = scheduleRepository;
        this.doctorRepository = doctorRepository;
    }


    @Transactional
    public ScheduledDto.ScheduleResponse createSchedule(Long doctorId,
                                                        ScheduledDto.ScheduleRequest request) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + doctorId));

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        if (!request.getScheduleDate().equals(today)
                && !request.getScheduleDate().equals(tomorrow)) {
            throw new InvalidOperationException(
                    "Schedule can only be created for today or tomorrow");
        }

        if (scheduleRepository.existsByDoctorIdAndScheduleDate(
                doctorId, request.getScheduleDate())) {
            throw new InvalidOperationException(
                    "Schedule already exists for this date");
        }

        if (request.getSlotDuration() < 10 || request.getSlotDuration() > 120) {
            throw new InvalidOperationException(
                    "Slot duration must be between 10 and 120 minutes");
        }

        DoctorSchedule schedule = DoctorSchedule.builder()
                .doctor(doctor)
                .scheduleDate(request.getScheduleDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .duration(request.getSlotDuration())
                .build();

        DoctorSchedule saved = scheduleRepository.save(schedule);

        return toResponse(saved);
    }


    public ScheduledDto.ScheduleResponse getDoctorSchedule(Long doctorId,
                                                     LocalDate date) {

        DoctorSchedule schedule = scheduleRepository
                .findByDoctorIdAndScheduleDate(doctorId, date)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No schedule found for this doctor on " + date));

        return toResponse(schedule);
    }



    private ScheduledDto.ScheduleResponse toResponse(DoctorSchedule schedule) {

        return ScheduledDto.ScheduleResponse.builder()
                .id(schedule.getId())
                .scheduleDate(schedule.getScheduleDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .Slots(generateSlots(schedule))
                .build();
    }


    public List<LocalTime> generateSlots(DoctorSchedule schedule) {

        List<LocalTime> slots = new ArrayList<>();

        LocalTime current = schedule.getStartTime();

        while (!current.isAfter(
                schedule.getEndTime().minusMinutes(schedule.getDuration()))) {

            slots.add(current);
            current = current.plusMinutes(schedule.getDuration());
        }

        return slots;
    }
}