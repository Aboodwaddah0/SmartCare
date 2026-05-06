package com.example.appointment.service;

import com.example.appointment.dto.ScheduledDto;
import com.example.appointment.entity.DoctorSchedule;
import com.example.appointment.exception.InvalidOperationException;
import com.example.appointment.exception.ResourceNotFoundException;
import com.example.appointment.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorScheduleService {

	private final ScheduleRepository scheduleRepository;

	public DoctorScheduleService(ScheduleRepository scheduleRepository) {
		this.scheduleRepository = scheduleRepository;
	}

	@Transactional
	public ScheduledDto.ScheduleResponse createSchedule(Long doctorId, ScheduledDto.ScheduleRequest request) {
		LocalDate today = LocalDate.now();
		LocalDate tomorrow = today.plusDays(1);

		if (!request.getScheduleDate().equals(today) && !request.getScheduleDate().equals(tomorrow)) {
			throw new InvalidOperationException("Schedule can only be created for today or tomorrow");
		}

		if (scheduleRepository.existsByDoctorIdAndScheduleDate(doctorId, request.getScheduleDate())) {
			throw new InvalidOperationException("Schedule already exists for this date");
		}

		if (request.getSlotDuration() < 10 || request.getSlotDuration() > 120) {
			throw new InvalidOperationException("Slot duration must be between 10 and 120 minutes");
		}

		DoctorSchedule schedule = DoctorSchedule.builder()
				.doctorId(doctorId)
				.scheduleDate(request.getScheduleDate())
				.startTime(request.getStartTime())
				.endTime(request.getEndTime())
				.duration(request.getSlotDuration())
				.build();

		DoctorSchedule saved = scheduleRepository.save(schedule);
		return toResponse(saved);
	}

	public ScheduledDto.ScheduleResponse getDoctorSchedule(Long doctorId, LocalDate date) {
		DoctorSchedule schedule = scheduleRepository.findByDoctorIdAndScheduleDate(doctorId, date)
				.orElseThrow(() -> new ResourceNotFoundException("No schedule found for this doctor on " + date));
		return toResponse(schedule);
	}

	private ScheduledDto.ScheduleResponse toResponse(DoctorSchedule schedule) {
		return ScheduledDto.ScheduleResponse.builder()
				.id(schedule.getId())
				.doctorId(schedule.getDoctorId())
				.scheduleDate(schedule.getScheduleDate())
				.startTime(schedule.getStartTime())
				.endTime(schedule.getEndTime())
				.Slots(generateSlots(schedule))
				.build();
	}

	public List<LocalTime> generateSlots(DoctorSchedule schedule) {
		List<LocalTime> slots = new ArrayList<>();
		LocalTime current = schedule.getStartTime();
		while (!current.isAfter(schedule.getEndTime().minusMinutes(schedule.getDuration()))) {
			slots.add(current);
			current = current.plusMinutes(schedule.getDuration());
		}
		return slots;
	}
}
