package com.medbay.controller;

import com.medbay.domain.AppointmentInfo;
import com.medbay.domain.request.CreateAppointmentInfoRequest;
import com.medbay.service.AppointmentInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;


@RestController
@RequestMapping("/appointmentInfo")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AppointmentInfoController {

    private final AppointmentInfoService appointmentInfoService;


    @GetMapping
    public ResponseEntity<List<AppointmentInfo>> getAppointmentInfo(@RequestBody CreateAppointmentInfoRequest request) {
        return ResponseEntity.ok(appointmentInfoService.getAppointmentInfo(request));
    }

//    @GetMapping("/admin/calendar")
//    public ResponseEntity<List<Integer>> getNumAppointmentsPerDay(@RequestBody CreateAppointmentInfoRequest request){
//        return ResponseEntity.ok(appointmentInfoService.getNumAppointmentsPerDay(request));
//    }


    @PostMapping
    public ResponseEntity<Void> createAppointmentInfo(@RequestBody CreateAppointmentInfoRequest request) {
        return appointmentInfoService.createAppointmentInfo(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointmentInfo(@PathVariable("id") Long id) {
        return appointmentInfoService.deleteAppointmentInfo(id);
    }
}
