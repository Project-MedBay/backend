package com.medbay.controller;


import com.medbay.domain.Appointment;
import com.medbay.domain.request.CreateAppointmentRequest;
import com.medbay.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<Appointment>> getAppointments() {
        return appointmentService.getAppointments();
    }

    @PostMapping
    public ResponseEntity<Void> createAppointment(@RequestBody CreateAppointmentRequest appointment) {
        return appointmentService.createAppointment(appointment);
    }
    @GetMapping("/employee/calendar")
    public ResponseEntity<List<Appointment>> getAllAppointmentsPerTimeSlot(@RequestBody LocalDateTime dateTime) {
        return appointmentService.getAllAppointmentsPerTimeSlot(dateTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable("id") Long id) {
        return appointmentService.deleteAppointment(id);
    }

    @PutMapping("/{appointmentId}/description")
    public ResponseEntity<Void> updateAppointmentDescription(
            @PathVariable Long appointmentId,
            @RequestBody String newDescription) {

        appointmentService.updateAppointmentDescription(appointmentId, newDescription);
        return ResponseEntity.ok().build();
    }
}
