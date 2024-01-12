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

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()));

        Employee employee = isEmployee ? (Employee) authentication.getPrincipal() : null;

        List<PatientDTO> patientDTOs = patients.stream()
                .map(patient -> createPatientDTO(patient, isEmployee, isAdmin, employee))
                .collect(Collectors.toList());

        return ResponseEntity.ok(patientDTOs);
    }

    private PatientDTO createPatientDTO(Patient patient, boolean isEmployee, boolean isAdmin, Employee employee) {
        boolean hasAppointmentWithEmployee = isEmployee && patient.getAppointments().stream()
                .anyMatch(appointment -> appointment.getEmployee() != null && appointment.getEmployee().equals(employee));

        return PatientDTO.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .createdAt(patient.getCreatedAt())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth())
                .OIB(patient.getOIB())
                .MBO(patient.getMBO())
                .phoneNumber(patient.getPhoneNumber())
                .show(hasAppointmentWithEmployee || isAdmin)
                .build();
    }


    public ResponseEntity<PatientDTO> getLoggedInPatient() {
        Patient patient = (Patient) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PatientDTO patientDTO = PatientDTO.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .createdAt(patient.getCreatedAt())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth())
                .OIB(patient.getOIB())
                .MBO(patient.getMBO())
                .phoneNumber(patient.getPhoneNumber())
                .build();
        return ResponseEntity.ok(patientDTO);
    }

    public ResponseEntity<Void> updatePatient(PatientDTO patient) {
        Patient patientToUpdate = patientRepository.findById(patient.getId()).orElse(null);
        if (patientToUpdate == null) {
            return ResponseEntity.notFound().build();
        }

        patientToUpdate.setFirstName(patient.getFirstName());
        patientToUpdate.setLastName(patient.getLastName());
        patientToUpdate.setEmail(patient.getEmail());
        patientToUpdate.setAddress(patient.getAddress());
        patientToUpdate.setDateOfBirth(patient.getDateOfBirth());
        patientToUpdate.setPhoneNumber(patient.getPhoneNumber());
        patientToUpdate.setOIB(patient.getOIB());
        patientToUpdate.setMBO(patient.getMBO());

        patientRepository.save(patientToUpdate);
        return ResponseEntity.ok().build();
    }
}
