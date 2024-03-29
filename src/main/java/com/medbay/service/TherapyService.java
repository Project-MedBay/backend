package com.medbay.service;

import com.medbay.domain.*;
import com.medbay.domain.DTO.PatientDTO;
import com.medbay.domain.DTO.TherapyDTO;
import com.medbay.domain.DTO.VerificationsDTO;
import com.medbay.domain.enums.ActivityStatus;
import com.medbay.domain.enums.Specialization;
import com.medbay.domain.enums.TherapyStatus;
import com.medbay.domain.request.CreateTherapyRequest;
import com.medbay.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.medbay.domain.enums.TherapyStatus.PENDING;
import static org.springframework.http.HttpStatus.CONFLICT;

@Service
@RequiredArgsConstructor
public class TherapyService {
    private final TherapyRepository therapyRepository;
    private final TherapyTypeRepository therapyTypeRepository;
    private final EmailService emailService;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final EmployeeRepository employeeRepository;
    private final HealthReferralRepository healthReferralRepository;
    private final DoctorRepository doctorRepository;

    private final Random random = new Random();

    public ResponseEntity<List<Therapy>> getTherapies() {
        List<Therapy> therapies = therapyRepository.findAll();
        return ResponseEntity.ok(therapies);
    }

    public ResponseEntity<Void> deleteTherapy(Long id) {
        if (!therapyRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        therapyRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<VerificationsDTO> getRequests() {
        List<TherapyDTO> therapyDTOS = therapyRepository.findByTherapyStatus(PENDING)
                .stream()
                .map(therapy -> {
                    List<LocalDateTime> sessionDates = therapy.getAppointments().stream()
                            .map(Appointment::getDateTime)
                            .sorted()
                            .collect(Collectors.toList());

                    return TherapyDTO.builder()
                            .therapyId(therapy.getId())
                            .patientId(therapy.getPatient().getId())
                            .therapyTypeCode(therapy.getTherapyType().getTherapyCode())
                            .therapyTypeName(therapy.getTherapyType().getName())
                            .numberOfSessions(therapy.getTherapyType().getNumOfSessions())
                            .requestDate(therapy.getRequestDate())
                            .sessionDates(sessionDates)
                            .build();
                })
                .collect(Collectors.toList());

        therapyDTOS.sort(Comparator.comparing(TherapyDTO::getRequestDate));

        List<PatientDTO> patients = patientRepository.findAllByStatus(ActivityStatus.PENDING)
                .stream()
                .map(patient -> PatientDTO.builder()
                        .id(patient.getId())
                        .firstName(patient.getFirstName())
                        .lastName(patient.getLastName())
                        .email(patient.getEmail())
                        .MBO(patient.getMBO())
                        .dateOfBirth(patient.getDateOfBirth())
                        .address(patient.getAddress())
                        .phoneNumber(patient.getPhoneNumber())
                        .createdAt(patient.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        patients.sort(Comparator.comparing(PatientDTO::getCreatedAt));

        VerificationsDTO pendingVerifications = VerificationsDTO.builder()
                .patients(patients)
                .therapies(therapyDTOS)
                .build();

        return ResponseEntity.ok(pendingVerifications);
    }

    public ResponseEntity<String> changeTherapyStatus(Long id, String status, String rejectionReason) {
        Therapy therapy = therapyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Therapy not found"));
        if(status.equals(TherapyStatus.DECLINED.name())) {
            if(rejectionReason == null || rejectionReason.isEmpty()) {
                return ResponseEntity.status(CONFLICT).body("Rejection reason is required.");
            }
            List<Appointment> appointments = therapy.getAppointments();
            therapyRepository.delete(therapy);
            appointmentRepository.deleteAll(appointments);
            emailService.sendTherapyRejectionEmail(therapy.getPatient(), rejectionReason);
        }
        else{
            therapy.setTherapyStatus(TherapyStatus.VERIFIED);
            therapyRepository.save(therapy);
            emailService.sendTherapyConfirmationEmail(therapy.getPatient());
        }
        emailService.sendTherapyConfirmationEmail(therapy.getPatient());
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<String> createNewTherapy(CreateTherapyRequest request) {
        Patient patient = (Patient) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ResponseEntity<String> validationResponse = validateRequest(request);
        if (validationResponse != null) {
            return validationResponse;
        }

        TherapyType therapyType = therapyTypeRepository.findByTherapyCode(request.getTherapyCode())
                .orElseThrow(() -> new RuntimeException("Therapy type not found with code: " + request.getTherapyCode()));
        List<Appointment> appointments = createAppointments(request, patient, therapyType);

        if (appointments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Unable to schedule any appointments.");
        }

        Therapy therapy = Therapy.builder()
                .requestDate(LocalDateTime.now().plusHours(1))
                .therapyStatus(PENDING)
                .patient(patient)
                .therapyType(therapyType)
                .build();

        therapyRepository.save(therapy);

        appointments.forEach(appointment -> appointment.setTherapy(therapy));
        appointmentRepository.saveAll(appointments);

        return ResponseEntity.ok().build();
    }

    private ResponseEntity<String> validateRequest(CreateTherapyRequest request) {

        if(!doctorRepository.existsByHlkid(request.getHlkid())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Doctor not found by hlkid.");
        }

        if (!doctorRepository.isActiveById(request.getHlkid())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Doctor is not active.");
        }

        if (!healthReferralRepository.existsByHlkidAndHealthReferralIdAndTherapyCode(
                request.getHlkid(), request.getHealthReferralId(), request.getTherapyCode())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Health referral not found.");
        }

        return null;
    }

    private List<Appointment> createAppointments(CreateTherapyRequest request, Patient patient, TherapyType therapyType) {
        List<Appointment> appointments = new ArrayList<>();
        Specialization specialization = therapyType.getRequiredEquipment().getSpecialization();

        for (LocalDateTime dateTime : request.getAppointmentDates()) {
            List<Employee> employees = employeeRepository.findAllByAppointmentsDateTimeAndSpecialization(
                    dateTime, specialization);


            if (employees.isEmpty()) {
                return new ArrayList<>();
            }

            Appointment appointment = Appointment.builder()
                    .patient(patient)
                    .sessionNotes("")
                    .dateTime(dateTime.plusHours(1))
                    .employee(employees.get(random.nextInt(employees.size())))
                    .build();
            appointments.add(appointment);
        }

        return appointments;
    }


}
