package com.medbay.controller;


import com.medbay.domain.DTO.PatientDTO;
import com.medbay.domain.DTO.PatientProfileDTO;
import com.medbay.domain.DTO.PatientSessionsDTO;
import com.medbay.domain.Patient;
import com.medbay.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PatientController {
    private final PatientService patientService;

    @GetMapping("/all")
    public ResponseEntity<List<PatientDTO>> getPatients(){
        return patientService.getPatients();
    }


    @GetMapping("/dashboard")
    public ResponseEntity<Map<LocalDate, List<PatientSessionsDTO>>> getPatientsDashboard(){
        return patientService.getPatientDashboard();
    }

    @PutMapping
    public ResponseEntity<Void> updatePatient(@RequestBody PatientDTO patient){
        return patientService.updatePatient(patient);
    }

    @GetMapping("/logged-in")
    public ResponseEntity<PatientProfileDTO> getLoggedInPatient(){
        return patientService.getLoggedInPatient();
    }

}
