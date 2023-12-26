package com.medbay.service;

import com.medbay.domain.*;
import com.medbay.domain.enums.ActivityStatus;
import com.medbay.domain.request.CreateTherapyRequest;
import com.medbay.repository.AppointmentRepository;
import com.medbay.repository.EmployeeRepository;
import com.medbay.repository.PatientRepository;
import com.medbay.repository.TherapyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public ResponseEntity<Void> createTherapy(CreateTherapyRequest request){
        Optional<Patient> patient = patientRepository.findById(request.getPatientId());
        if(patient.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Employee> employee = employeeRepository.findById(request.getEmployeeId());
        if(employee.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Appointment> appointment = appointmentRepository.findById(request.getAppointmentId());
        if(appointment.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Therapy therapy = Therapy.builder()
                .therapyType(request.getTherapyType())
                .build();
        therapyRepository.save(therapy);
        return ResponseEntity.ok().build();
    }
    public ResponseEntity<Void> deleteTherapy(Long id){
        if(!therapyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        therapyRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<Therapy>> getTherapyRequests() {
        List<Therapy> allTherapies = therapyRepository.findAll();
        List<Therapy> therapyRequests = new ArrayList<>();

        for(Therapy therapy : allTherapies) {
            for(Appointment appointment : therapy.getAppointments()) {
                if (appointment.getStatus().equals(ActivityStatus.PENDING)) {
                    therapyRequests.add(therapy);
                    break;
                }
            }
        }
        return ResponseEntity.ok(therapyRequests);
    }
}
