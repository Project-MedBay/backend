package com.medbay.controller;

import com.medbay.domain.AppointmentInfo;
import com.medbay.domain.request.CreateAppointmentInfoRequest;
import com.medbay.service.AppointmentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;


@RestController
@RequestMapping("/appointmentInfo")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AppointmentInfoController {

    private final AppointmentInfoService appointmentInfoService;


    @GetMapping
    public ResponseEntity<List<AppointmentInfo>> getAppointmentInfo(@RequestBody CreateAppointmentInfoRequest request) {
        List<AppointmentInfo> appointmentInfos;

        if (request.getAppointmentDate() == null) {
            // Ako datum nije odabran, dohvati sve termine za trenutni mjesec
            LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
            LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

            appointmentInfos = appointmentInfoService.getAppointmentInfoForMonth(firstDayOfMonth, lastDayOfMonth, request.getEquipmentId());
        } else {
            // Ako je odabran određeni datum, dohvati termine za taj datum
            appointmentInfos = appointmentInfoService.getAppointmentInfo(request.getAppointmentDate());

            // Ako nema termina za odabrani datum, generiraj ih
            if (appointmentInfos.isEmpty()) {
                appointmentInfos = appointmentInfoService.generateAppointmentInfo(request.getAppointmentDate(), request.getEquipmentId());
            }
        }

        return ResponseEntity.ok(appointmentInfos);
    }


    @PostMapping
    public ResponseEntity<Void> createAppointmentInfo(@RequestBody CreateAppointmentInfoRequest request) {
        return appointmentInfoService.createAppointmentInfo(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointmentInfo(@PathVariable("id") Long id) {
        return appointmentInfoService.deleteAppointmentInfo(id);
    }
}
