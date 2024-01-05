package com.medbay.service;

import com.medbay.domain.Appointment;
import com.medbay.domain.Employee;
import com.medbay.domain.Patient;
import com.medbay.domain.Therapy;
import com.medbay.domain.request.CreateAppointmentRequest;
import com.medbay.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId).get();
    }

    public ResponseEntity<List<Appointment>> getAppointmentsFromEmployee(Employee employee){
        return ResponseEntity.ok(employee.getAppointments());
    }
    public ResponseEntity<List<Appointment>> getAppointmentsPerTimeSlot(LocalDateTime dateTime) {
        List<Appointment> appointments = appointmentRepository.findByDateTime(dateTime);
        return ResponseEntity.ok(appointments);
    }
    public ResponseEntity<Integer> getNumOfAppointmentsPerTimeSlot(LocalDateTime dateTime) {
//        LocalDate ld;
//        DateTimeFormatter df = new DateTimeFormatterBuilder()
//                // case insensitive to parse JAN and FEB
//                .parseCaseInsensitive()
//                // add pattern
//                .appendPattern("\"yyyy-MM-dd\"")
//                // create formatter (use English Locale to parse month names)
//                .toFormatter(Locale.ENGLISH);
//        ld = LocalDate.parse(date, df);
        List<Appointment> appointments = appointmentRepository.findByDateTime(dateTime);
        return ResponseEntity.ok(appointments.size());
    }

    public ResponseEntity<List<Integer>> getNumOfAppointments(LocalDate localDate) {
        List<Integer> count = new ArrayList<>();
        for(int i = 8; i<20; i++) {
            List<Appointment> appointments = appointmentRepository.findByDateTime(localDate.atTime(i, 0));
            count.add(appointments.size());
        }
        return ResponseEntity.ok(count);
    }


    public ResponseEntity<List<Appointment>> getAllAppointmentsPerTimeSlot(LocalDateTime dateTime) {
        List<Appointment> appointments = appointmentRepository.findByDateTime(dateTime);
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

    public void updateAppointmentDescription(Long appointmentId, String newDescription) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

            //!!!!!!!!!!appointment.setDescription(newDescription); ovo sam komentirao jer koliko shvacam appointment jos nema description u sebi
            appointmentRepository.save(appointment);
    }

    public void rescheduleAppointment(Long appointmentId, LocalDateTime newDateTime) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));


        appointment.setDateTime(newDateTime);
        appointmentRepository.save(appointment);
    }

}
