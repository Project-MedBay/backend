package com.medbay.controller;


import com.medbay.domain.Appointment;
import com.medbay.domain.request.CreateAppointmentRequest;
import com.medbay.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable("id") Long id) {
        return appointmentService.deleteAppointment(id);
    }

}
