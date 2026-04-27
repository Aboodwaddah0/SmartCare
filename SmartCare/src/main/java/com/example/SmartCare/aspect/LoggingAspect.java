package com.example.SmartCare.aspect;

import com.example.SmartCare.dto.AppointmentDto;
import com.example.SmartCare.dto.PrescriptionDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger appointmentLogger = LoggerFactory.getLogger("APPOINTMENT");
    private static final Logger prescriptionLogger = LoggerFactory.getLogger("PRESCRIPTION");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @org.aspectj.lang.annotation.Before("execution(* com.example.SmartCare.service.AppointmentService.bookAppointment(..))")
    public void logAppointmentBookingAttempt(JoinPoint joinPoint) {
        AppointmentDto.BookingRequest request = (AppointmentDto.BookingRequest) joinPoint.getArgs()[0];
        appointmentLogger.info("[{}] USER: {} | ACTION: BOOK_APPOINTMENT_ATTEMPT | DoctorID: {} | PatientID: {} | Date: {} | Time: {}",
                getTimestamp(), getCurrentUsername(), request.getDoctorId(), request.getPatientId(),
                request.getDate(), request.getTime());
    }

    @org.aspectj.lang.annotation.AfterReturning(pointcut = "execution(* com.example.SmartCare.service.AppointmentService.bookAppointment(..))", returning = "result")
    public void logAppointmentBookingSuccess(JoinPoint joinPoint, Object result) {
        AppointmentDto.BookingRequest request = (AppointmentDto.BookingRequest) joinPoint.getArgs()[0];
        appointmentLogger.info("[{}] USER: {} | ACTION: BOOK_APPOINTMENT_SUCCESS | DoctorID: {} | PatientID: {} | Date: {} | Time: {}",
                getTimestamp(), getCurrentUsername(), request.getDoctorId(), request.getPatientId(),
                request.getDate(), request.getTime());
    }

    @org.aspectj.lang.annotation.AfterThrowing(pointcut = "execution(* com.example.SmartCare.service.AppointmentService.bookAppointment(..))", throwing = "ex")
    public void logAppointmentBookingFailed(JoinPoint joinPoint, Exception ex) {
        AppointmentDto.BookingRequest request = (AppointmentDto.BookingRequest) joinPoint.getArgs()[0];
        appointmentLogger.info("[{}] USER: {} | ACTION: BOOK_APPOINTMENT_FAILED | DoctorID: {} | PatientID: {} | Date: {} | Time: {} | ERROR: {}",
                getTimestamp(), getCurrentUsername(), request.getDoctorId(), request.getPatientId(),
                request.getDate(), request.getTime(), ex.getMessage());
    }

    @org.aspectj.lang.annotation.Before("execution(* com.example.SmartCare.service.AppointmentService.cancel(..))")
    public void logAppointmentCancellationAttempt(JoinPoint joinPoint) {
        Long appointmentId = (Long) joinPoint.getArgs()[0];
        appointmentLogger.info("[{}] USER: {} | ACTION: CANCEL_APPOINTMENT_ATTEMPT | AppointmentID: {}",
                getTimestamp(), getCurrentUsername(), appointmentId);
    }

    @org.aspectj.lang.annotation.AfterReturning(pointcut = "execution(* com.example.SmartCare.service.AppointmentService.cancel(..))")
    public void logAppointmentCancellationSuccess(JoinPoint joinPoint) {
        Long appointmentId = (Long) joinPoint.getArgs()[0];
        appointmentLogger.info("[{}] USER: {} | ACTION: CANCEL_APPOINTMENT_SUCCESS | AppointmentID: {}",
                getTimestamp(), getCurrentUsername(), appointmentId);
    }

    @org.aspectj.lang.annotation.AfterThrowing(pointcut = "execution(* com.example.SmartCare.service.AppointmentService.cancel(..))", throwing = "ex")
    public void logAppointmentCancellationFailed(JoinPoint joinPoint, Exception ex) {
        Long appointmentId = (Long) joinPoint.getArgs()[0];
        appointmentLogger.info("[{}] USER: {} | ACTION: CANCEL_APPOINTMENT_FAILED | AppointmentID: {} | ERROR: {}",
                getTimestamp(), getCurrentUsername(), appointmentId, ex.getMessage());
    }

    @org.aspectj.lang.annotation.Before("execution(* com.example.SmartCare.service.PrescriptionService.createPrescription(..))")
    public void logPrescriptionCreationAttempt(JoinPoint joinPoint) {
        PrescriptionDto.prescriptionRequest request = (PrescriptionDto.prescriptionRequest) joinPoint.getArgs()[0];
        prescriptionLogger.info("[{}] USER: {} | ACTION: CREATE_PRESCRIPTION_ATTEMPT | AppointmentID: {}",
                getTimestamp(), getCurrentUsername(), request.getAppointmentId());
    }

    @org.aspectj.lang.annotation.AfterReturning(pointcut = "execution(* com.example.SmartCare.service.PrescriptionService.createPrescription(..))", returning = "result")
    public void logPrescriptionCreationSuccess(JoinPoint joinPoint, Object result) {
        PrescriptionDto.prescriptionRequest request = (PrescriptionDto.prescriptionRequest) joinPoint.getArgs()[0];
        prescriptionLogger.info("[{}] USER: {} | ACTION: CREATE_PRESCRIPTION_SUCCESS | AppointmentID: {}",
                getTimestamp(), getCurrentUsername(), request.getAppointmentId());
    }

    @org.aspectj.lang.annotation.AfterThrowing(pointcut = "execution(* com.example.SmartCare.service.PrescriptionService.createPrescription(..))", throwing = "ex")
    public void logPrescriptionCreationFailed(JoinPoint joinPoint, Exception ex) {
        PrescriptionDto.prescriptionRequest request = (PrescriptionDto.prescriptionRequest) joinPoint.getArgs()[0];
        prescriptionLogger.info("[{}] USER: {} | ACTION: CREATE_PRESCRIPTION_FAILED | AppointmentID: {} | ERROR: {}",
                getTimestamp(), getCurrentUsername(), request.getAppointmentId(), ex.getMessage());
    }

    @org.aspectj.lang.annotation.Before("execution(* com.example.SmartCare.service.PrescriptionService.updatePrescription(..))")
    public void logPrescriptionUpdateAttempt(JoinPoint joinPoint) {
        String prescriptionId = (String) joinPoint.getArgs()[0];
        prescriptionLogger.info("[{}] USER: {} | ACTION: UPDATE_PRESCRIPTION_ATTEMPT | PrescriptionID: {}",
                getTimestamp(), getCurrentUsername(), prescriptionId);
    }

    @org.aspectj.lang.annotation.AfterReturning(pointcut = "execution(* com.example.SmartCare.service.PrescriptionService.updatePrescription(..))", returning = "result")
    public void logPrescriptionUpdateSuccess(JoinPoint joinPoint, Object result) {
        String prescriptionId = (String) joinPoint.getArgs()[0];
        prescriptionLogger.info("[{}] USER: {} | ACTION: UPDATE_PRESCRIPTION_SUCCESS | PrescriptionID: {}",
                getTimestamp(), getCurrentUsername(), prescriptionId);
    }

    @org.aspectj.lang.annotation.AfterThrowing(pointcut = "execution(* com.example.SmartCare.service.PrescriptionService.updatePrescription(..))", throwing = "ex")
    public void logPrescriptionUpdateFailed(JoinPoint joinPoint, Exception ex) {
        String prescriptionId = (String) joinPoint.getArgs()[0];
        prescriptionLogger.info("[{}] USER: {} | ACTION: UPDATE_PRESCRIPTION_FAILED | PrescriptionID: {} | ERROR: {}",
                getTimestamp(), getCurrentUsername(), prescriptionId, ex.getMessage());
    }

    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymous".equals(auth.getPrincipal())) {
            return auth.getName();
        }
        return "anonymous";
    }

    private String getTimestamp() {
        return LocalDateTime.now().format(FORMATTER);
    }
}