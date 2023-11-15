package com.medbay.service;

import com.medbay.domain.Appointment;
import com.medbay.domain.Employee;
import com.medbay.domain.Session;
import com.medbay.domain.request.CreateSessionRequest;
import com.medbay.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final TherapyRepository therapyRepository;
    private final EmployeeRepository employeeRepository;

    public ResponseEntity<List<Session>> getSessions() {
        List<Session> sessions = sessionRepository.findAll();
        return ResponseEntity.ok(sessions);
    }

    public ResponseEntity<Void> createSession(CreateSessionRequest request) {
        Optional<Appointment> appointment = appointmentRepository.findById(request.getAppointmentId());
        if (appointment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Employee> employee = employeeRepository.findById(request.getEmployeeId());
        if (employee.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Session session = Session.builder()
                .dateTime(request.getDateTime())
                .feedback(request.getFeedback())
                .employee(employee.get())
                .appointment(appointment.get())
                .build();

        sessionRepository.save(session);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteSession(Long id){
        if(!sessionRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        sessionRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
