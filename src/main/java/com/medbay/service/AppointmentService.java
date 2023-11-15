package com.medbay.service;

import com.medbay.domain.Appointment;
import com.medbay.domain.Employee;
import com.medbay.domain.Patient;
import com.medbay.domain.Therapy;
import com.medbay.domain.request.CreateAppointmentRequest;
import com.medbay.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final TherapyRepository therapyRepository;
    private final EmployeeRepository employeeRepository;


    public ResponseEntity<List<Appointment>> getAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return ResponseEntity.ok(appointments);
    }


    public ResponseEntity<Void> createAppointment(CreateAppointmentRequest request) {

        Optional<Patient> patient = patientRepository.findById(request.getPatientId());
        if(patient.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Therapy> therapy = therapyRepository.findById(request.getTherapyId());
        if(therapy.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Employee> employee = employeeRepository.findById(request.getEmployeeId());
        if(employee.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


        Appointment appointment = Appointment.builder()
                .dateTime(request.getDateTime())
                .status(request.getStatus())
                .patient(patient.get())
                .therapy(therapy.get())
                .employee(employee.get())
                .build();

        appointmentRepository.save(appointment);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteAppointment(Long id) {

        if(!appointmentRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        appointmentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
