package com.medbay.service;

import com.medbay.domain.*;
import com.medbay.domain.enums.TherapyStatus;
import com.medbay.domain.request.CreateTherapyRequest;
import com.medbay.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TherapyService {
    private final TherapyRepository therapyRepository;
    private final  TherapyTypeRepository therapyTypeRepository;

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final EmployeeRepository employeeRepository;
    public ResponseEntity<List<Therapy>> getTherapies() {
        List<Therapy> therapies = therapyRepository.findAll();
        return ResponseEntity.ok(therapies);
    }
/*
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

 */
    public ResponseEntity<Void> deleteTherapy(Long id){
        if(!therapyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        therapyRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> createNewTherapy(CreateTherapyRequest request) {
      //  Optional<Patient> patientOptional = patientRepository.findById(request.getPatientId());
        //Optional<Employee> employeeOptional = employeeRepository.findById(request.getEmployeeId());

        if (request.getTherapyCode().isEmpty() || request.getAppointmentDate().toString().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        LocalDateTime appointmentDateTime = request.getAppointmentDate();
TherapyType therapyType = therapyTypeRepository.findByTherapyCode(request.getTherapyCode());
        Therapy therapy = Therapy.builder()
                .therapyStatus(TherapyStatus.PENDING)
                .therapyType(therapyType)
                .build();

     User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Patient patient = (Patient)user;
        // Dodajem terapiju pacijentu
        patient.addTherapy(therapy);

        //Dodajem terapiju employee, na lraju ne znam jel se nama salje employeeId pa ne stvaram to tu
        //employee.addTherapy(therapy);

        // Spremi ažuriranog pacijenta u repozitorij
        patientRepository.save(patient);

        // Slanje zahtjeva adminu
        sendRequestToAdmin(request.getTherapyCode(), appointmentDateTime, patient.getMBO());

        return ResponseEntity.ok().build();
    }

    private void sendRequestToAdmin(String therapyCode, LocalDateTime therapyDate, String patientMBO) {
        boolean verify = false;
        //tu tzreba provjera zeli li admin odobriti, ne znam kako su to na frontu planirali simulirati pa sam samo postavila boolean verify
        TherapyType therapyType = therapyTypeRepository.findByTherapyCode(therapyCode);
        Therapy therapy = therapyRepository.findByTherapyType(therapyType);

        if (verify) {
            therapy.setTherapyStatus(TherapyStatus.VERIFIED);
        }

        therapyRepository.save(therapy);
    }
}
