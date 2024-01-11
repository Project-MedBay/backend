package com.medbay.controller;


import com.medbay.domain.DTO.PatientDTO;
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
    public ResponseEntity<List<PatientDTO>> getPatients(){
        return patientService.getPatients();
    }


    @GetMapping("/logged-in")
    public ResponseEntity<PatientDTO> getLoggedInPatient(){
        return patientService.getLoggedInPatient();
    }

}
