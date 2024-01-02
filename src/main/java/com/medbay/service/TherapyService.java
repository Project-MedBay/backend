package com.medbay.service;

import com.medbay.domain.Appointment;
import com.medbay.domain.Employee;
import com.medbay.domain.Patient;
import com.medbay.domain.Therapy;
import com.medbay.domain.enums.ActivityStatus;
import com.medbay.domain.request.CreateTherapyRequest;
import com.medbay.repository.AppointmentRepository;
import com.medbay.repository.EmployeeRepository;
import com.medbay.repository.PatientRepository;
import com.medbay.repository.TherapyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TherapyService {
    private final TherapyRepository therapyRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final EmployeeRepository employeeRepository;

    public ResponseEntity<List<Therapy>> getTherapies() {
        List<Therapy> therapies = therapyRepository.findAll();
        return ResponseEntity.ok(therapies);
    }

    public ResponseEntity<Void> createTherapy(CreateTherapyRequest request) {

        Optional<Patient> patient = patientRepository.findById(request.getPatientId());
        if (patient.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Employee> employee = employeeRepository.findById(request.getEmployeeId());
        if (employee.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Appointment> appointment = appointmentRepository.findById(request.getAppointmentId());
        if (appointment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Therapy therapy = Therapy.builder()
                .therapyType(request.getTherapyType())
                .build();
        therapyRepository.save(therapy);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteTherapy(Long id) {
        if (!therapyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        therapyRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<Therapy>> getTherapyRequests() {
        ;
        List<Appointment> appointments = appointmentRepository.findAppointmentsByStatus(ActivityStatus.PENDING);
        List<Long> therapyIds = new ArrayList<>();
        List<Therapy> pendingTherapies = new ArrayList<>();
        for (Appointment app : appointments) {
            Long ids = app.getTherapy().getId();
            therapyIds.add(ids);
        }
        therapyIds = therapyIds.stream().distinct().collect(Collectors.toList());
        for (Long id : therapyIds) {
            pendingTherapies.add(therapyRepository.getReferenceById(id));
        }

        /*
        List<Therapy> pendingTherapies = therapyRepository.findByTherapyStatus(TherapyStatus.PENDING);
         */

        return ResponseEntity.ok(pendingTherapies);
    }
}
