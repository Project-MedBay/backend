package com.medbay.controller;


import com.medbay.domain.DTO.AdminCalendarDTO;
import com.medbay.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/admin")
    public ResponseEntity<Map<LocalDateTime, List<AdminCalendarDTO>>> getAppointmentsForWeek(@RequestParam LocalDate date) {
        return appointmentService.getAppointmentsForWeek(date);
    }

    @GetMapping("/availability")
    public ResponseEntity<Map<String, List<Integer>>> getAvailability(@RequestParam String therapyCode,
                                                                      @RequestParam int days) {
        return appointmentService.getAvailability(therapyCode, days);
    }

    @GetMapping("/reschedule/{appointmentId}")
    public ResponseEntity<Map<String, List<Integer>>> getAvailabilityForReschedule(@PathVariable Long appointmentId) {
        return appointmentService.getAvailabilityForReschedule(appointmentId);
    }

    @PutMapping("/reschedule/{appointmentId}")
    public ResponseEntity<Void> rescheduleAppointment(@PathVariable Long appointmentId,
                                                      @RequestParam LocalDateTime newDateTime) {
        return appointmentService.rescheduleAppointment(appointmentId, newDateTime);
    }

    @PutMapping("/session-notes/{appointmentId}")
    public ResponseEntity<Void> updateAppointmentDescription(@PathVariable Long appointmentId,
                                                             @RequestParam String sessionNotes) {
        return appointmentService.updateAppointmentSessionNotes(appointmentId, sessionNotes);
    }
}
