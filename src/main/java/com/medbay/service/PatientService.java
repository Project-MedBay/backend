package com.medbay.service;

import com.medbay.domain.Patient;
import com.medbay.domain.User;
import com.medbay.domain.enums.ActivityStatus;
import com.medbay.domain.enums.Role;
import com.medbay.domain.request.CreatePatientRequest;
import com.medbay.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;


    public ResponseEntity<List<Patient>> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return ResponseEntity.ok(patients);
    }


    public ResponseEntity<List<Patient>> getPendingPatients() {
        List<Patient> patients = patientRepository.findAllByStatus(ActivityStatus.PENDING);
        return ResponseEntity.ok(patients);
    }
}
