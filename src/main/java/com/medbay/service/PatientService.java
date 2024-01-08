package com.medbay.service;

import com.medbay.domain.DTO.PatientDTO;
import com.medbay.domain.Employee;
import com.medbay.domain.Patient;
import com.medbay.domain.enums.ActivityStatus;
import com.medbay.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;


    public ResponseEntity<List<PatientDTO>> getPatients() {
        List<Patient> patients = patientRepository.findAllByStatus(ActivityStatus.ACTIVE);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isEmployee = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> "ROLE_STAFF".equals(auth.getAuthority()));

        Employee employee = isEmployee ? (Employee) authentication.getPrincipal() : null;

        List<PatientDTO> patientDTOs = patients.stream()
                .map(patient -> createPatientDTO(patient, isEmployee, employee))
                .collect(Collectors.toList());

        return ResponseEntity.ok(patientDTOs);
    }

    private PatientDTO createPatientDTO(Patient patient, boolean isEmployee, Employee employee) {
        boolean hasAppointmentWithEmployee = isEmployee && patient.getAppointments().stream()
                .anyMatch(appointment -> appointment.getEmployee() != null && appointment.getEmployee().equals(employee));

        return PatientDTO.builder()
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .createdAt(patient.getCreatedAt())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth())
                .OIB(patient.getOIB())
                .MBO(patient.getMBO())
                .phoneNumber(patient.getPhoneNumber())
                .show(hasAppointmentWithEmployee)
                .build();
    }


    public ResponseEntity<List<Patient>> getPendingPatients() {
        List<Patient> patients = patientRepository.findAllByStatus(ActivityStatus.PENDING);
        return ResponseEntity.ok(patients);
    }

}
