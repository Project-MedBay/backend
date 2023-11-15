package com.medbay.service;

import com.medbay.domain.Patient;
import com.medbay.domain.User;
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
    private final BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<List<Patient>> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return ResponseEntity.ok(patients);
    }

    public ResponseEntity<Void> createPatient(CreatePatientRequest request) {

        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Patient patient = Patient.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth())
                .MBO(request.getMBO())
                .phoneNumber(request.getPhoneNumber())
                .active(true)
                .role(Role.ROLE_PATIENT)
                .build();

        patientRepository.save(patient);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deletePatient(Long id) {

        if(!patientRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        patientRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
