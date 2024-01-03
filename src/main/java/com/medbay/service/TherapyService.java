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
    private  final   PatientService patientService;
    private final   EmailService emailService;
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
        patientService.addTherapy(therapy, patient.getId());

        //Dodajem terapiju employee, na lraju ne znam jel se nama salje employeeId pa ne stvaram to tu
        //employee.addTherapy(therapy);

        // Spremi ažuriranog pacijenta u repozitorij, ovo vec spremam pozivom addTherapy u patientRepositoryu
       // patientRepository.save(patient);

        // Slanje zahtjeva adminu, s fronta mi salju razlog i status mogu mijenati
        sendRequestToAdmin(request.getTherapyCode(), appointmentDateTime, patient.getMBO(), TherapyStatus.PENDING, " ");

        return ResponseEntity.ok().build();
    }

    private void sendRequestToAdmin(String therapyCode, LocalDateTime therapyDate, String patientMBO, TherapyStatus status, String reason) {
        TherapyType therapyType = therapyTypeRepository.findByTherapyCode(therapyCode);
        Therapy therapy = therapyRepository.findByTherapyType(therapyType);

        Patient patient = patientRepository.findByMBO(patientMBO);

        therapy.setTherapyStatus(status);

        // Ako je terapija odbijena, postavi razlog
        if (status == TherapyStatus.DECLINED) {
            therapy.setRejectionReason(reason);
        } else {
            // Ako je terapija odobrena, postavi razlog na null (ako već ima neki razlog)
            therapy.setRejectionReason(null);
        }

        emailService.sendTherapyConfirmationEmail(patient);

        // Spremam izmjene ako su se dogodile oko statusa ili RejectionReasona
        therapyRepository.save(therapy);
    }
}
