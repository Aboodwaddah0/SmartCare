package com.example.SmartCare.service;

import com.example.SmartCare.entity.Doctor;
import com.example.SmartCare.entity.DoctorAvailability;
import com.example.SmartCare.repository.DoctorAvailabilityRepository;
import com.example.SmartCare.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service

public class DoctorAvailabilityService {

  private final DoctorAvailabilityRepository doctorAvailabilityRepository;
  private final DoctorRepository doctorRepository;
    public DoctorAvailabilityService(DoctorAvailabilityRepository doctorAvailabilityRepository, DoctorRepository doctorRepository) {
        this.doctorAvailabilityRepository = doctorAvailabilityRepository;
        this.doctorRepository = doctorRepository;
    }


 public List<LocalTime> getAvailableSlots(Long doctorId, LocalDate date) {

     DayOfWeek day= date.getDayOfWeek();
        DoctorAvailability avl = doctorAvailabilityRepository.findByDoctorIdAndDayOfWeek(doctorId, date).orElseThrow(() -> new RuntimeException("No availability"));;
        List<LocalTime> bookedSlots  = doctorAvailabilityRepository.findBookedSlotsByDoctorIdAndDayOfWeek(doctorId, date).stream().toList();

        List<LocalTime> availableSlots = generateSlots(avl.getStartTime(), avl.getEndTime(), avl.getDuration());
        availableSlots.removeAll(bookedSlots);
        return availableSlots;




 }


    // helper function to generate appointment slots
    public List<LocalTime> generateSlots(LocalTime start, LocalTime end, int durationMinutes) {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime current = start;
        while (current.plusMinutes(durationMinutes).isBefore(end) ||
                current.plusMinutes(durationMinutes).equals(end)) {

            slots.add(current);
            current = current.plusMinutes(durationMinutes);
        }
        return slots;
    }


}
