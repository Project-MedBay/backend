package com.medbay.controller;


import com.medbay.domain.Patient;
import com.medbay.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PatientController {
    private final PatientService patientService;

    @GetMapping("/all")
    public ResponseEntity<List<Patient>> getPatients(){
        return patientService.getPatients();
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Patient>> getPendingPatients(){
        return patientService.getPendingPatients();
    }


}
