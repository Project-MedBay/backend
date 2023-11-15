package com.medbay.controller;


import com.medbay.domain.Patient;
import com.medbay.domain.request.CreatePatientRequest;
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

    @GetMapping
    public ResponseEntity<List<Patient>> getPatients(){
        return patientService.getPatients();
    }

    @PostMapping
    public ResponseEntity<Void> createPatient(@RequestBody CreatePatientRequest patient){
        return patientService.createPatient(patient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable("id") Long id) {
        return patientService.deletePatient(id);
    }
}
