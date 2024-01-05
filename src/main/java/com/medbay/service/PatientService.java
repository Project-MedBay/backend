package com.medbay.service;

import com.medbay.domain.Appointment;
import com.medbay.domain.Employee;
import com.medbay.domain.Patient;
import com.medbay.domain.Therapy;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    private final UserRepository userRepository;


    public ResponseEntity<List<Patient>> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        return ResponseEntity.ok(patients);
    }

    public ResponseEntity<List<Patient>> getPatientsFromEmployee(Employee employee) {
        List<Appointment> appointments = appointmentRepository.findByEmployeeId(employee.getId());

        List<Patient> patients = appointments.stream()
                .map(Appointment::getPatient)
                .distinct()
                .collect(Collectors.toList());

        return ResponseEntity.ok(patients);
    }




        public ResponseEntity<List<Patient>> getPendingPatients() {
        List<Patient> patients = patientRepository.findAllByStatus(ActivityStatus.PENDING);
        return ResponseEntity.ok(patients);
    }

    public void addTherapy(Therapy therapy, Long patientId) {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);

        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();

            if (patient.getTherapies() == null) {
                patient.setTherapies(new ArrayList<>());
            }
            patient.getTherapies().add(therapy);
            therapy.setPatient(patient);

            // Sačuvaj ažuriranog pacijenta sa dodatom terapijom
            patientRepository.save(patient);

        }
    }
}
