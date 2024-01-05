package com.medbay.controller;


import com.medbay.domain.Appointment;
import com.medbay.domain.request.CreateAppointmentRequest;
import com.medbay.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/admin/calendar")
    public ResponseEntity<List<Appointment>> getAppointmentsPerTimeSlot(@RequestBody LocalDateTime dateTime) {
        return appointmentService.getAppointmentsPerTimeSlot(dateTime);
    }

    @GetMapping("/admin/count")
    public ResponseEntity<Integer> getNumOfAppointmentsPerTimeSlot(@RequestBody LocalDateTime date) {
        return appointmentService.getNumOfAppointmentsPerTimeSlot(date);
    }

    @GetMapping("/sudoku")
    public ResponseEntity<List<Integer>> getNumOfAppointments(@RequestBody LocalDate date) {
        return appointmentService.getNumOfAppointments(date);
    }

    @PostMapping
    public ResponseEntity<Void> createAppointment(@RequestBody CreateAppointmentRequest appointment) {
        return appointmentService.createAppointment(appointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable("id") Long id) {
        return appointmentService.deleteAppointment(id);
    }

}
