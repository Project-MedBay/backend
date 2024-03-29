package com.medbay.service;

import com.medbay.domain.Appointment;
import com.medbay.domain.DTO.*;
import com.medbay.domain.Employee;
import com.medbay.domain.Patient;
import com.medbay.domain.Therapy;
import com.medbay.domain.enums.ActivityStatus;
import com.medbay.domain.enums.TherapyStatus;
import com.medbay.repository.*;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.medbay.util.Helper.log;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<List<PatientDTO>> getPatients() {
        List<Patient> patients = patientRepository.findAllByStatus(ActivityStatus.ACTIVE);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isEmployee = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> "STAFF".equals(auth.getAuthority()));

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> "ADMIN".equals(auth.getAuthority()));


        Employee employee = isEmployee ? (Employee) authentication.getPrincipal() : null;

        List<PatientDTO> patientDTOs = patients.stream()
                .map(patient -> createPatientDTO(patient, isEmployee, isAdmin, employee))
                .collect(Collectors.toList());

        if(isEmployee){
            patientDTOs.stream()
                    .filter(patient -> patient.getAppointments() != null)
                    .flatMap(patient -> patient.getAppointments().stream())
                    .filter(appointment -> appointment.getAppointmentDate().isBefore(LocalDateTime.now().minusMonths(6)))
                    .forEach(appointment -> appointment.setShow(false));
        }

        return ResponseEntity.ok(patientDTOs);
    }

    private PatientDTO createPatientDTO(Patient patient, boolean isEmployee, boolean isAdmin, Employee employee) {
        boolean hasAppointmentWithEmployee = isEmployee && patient.getAppointments().stream()
                .anyMatch(appointment ->  appointment.getEmployee().getId().equals(employee.getId()));

        return PatientDTO.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .createdAt(patient.getCreatedAt())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth())
                .MBO(patient.getMBO())
                .phoneNumber(patient.getPhoneNumber())
                .show(hasAppointmentWithEmployee || isAdmin)
                .appointments(patient.getAppointments().stream()
                        .map(appointment -> AppointmentDTO.builder()
                                .appointmentId(appointment.getId())
                                .appointmentDate(appointment.getDateTime())
                                .sessionNotes(appointment.getSessionNotes())
                                .therapyName(appointment.getTherapy().getTherapyType().getName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }


    public ResponseEntity<PatientProfileDTO> getLoggedInPatient() {
        Patient patient = (Patient) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PatientDTO patientDTO = PatientDTO.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .createdAt(patient.getCreatedAt())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth())
                .MBO(patient.getMBO())
                .photo(patient.getPhoto())
                .phoneNumber(patient.getPhoneNumber())
                .build();

        List<Therapy> therapies = patient.getTherapies().stream()
                .filter(therapy -> therapy.getTherapyStatus().equals(TherapyStatus.VERIFIED))
                .toList();

        List<TherapyDTO> therapyDTOs = therapies.stream()
                .map(therapy ->
                    TherapyDTO.builder()
                            .therapyId(therapy.getId())
                            .patientId(patient.getId())
                            .requestDate(therapy.getRequestDate())
                            .therapyTypeCode(therapy.getTherapyType().getTherapyCode())
                            .numberOfSessions(therapy.getTherapyType().getNumOfSessions())
                            .therapyTypeName(therapy.getTherapyType().getName())
                            .sessionDates(therapy.getAppointments().stream()
                                    .map(Appointment::getDateTime)
                                    .sorted(Comparator.comparing(dateTime -> dateTime))
                                    .collect(Collectors.toList()))
                            .sessionNotes(therapy.getAppointments().stream()
                                    .map(Appointment::getSessionNotes)
                                    .sorted(Comparator.comparing(dateTime -> dateTime))
                                    .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        PatientProfileDTO patientProfileDTO = PatientProfileDTO.builder()
                .patient(patientDTO)
                .therapies(therapyDTOs)
                .build();

        return ResponseEntity.ok(patientProfileDTO);
    }

    public ResponseEntity<Void> updatePatient(PatientDTO patient) {
        Patient patientToUpdate = (Patient) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        patientToUpdate.setFirstName(patient.getFirstName());
        patientToUpdate.setLastName(patient.getLastName());
        patientToUpdate.setPassword(passwordEncoder.encode(patient.getPassword()));
        patientToUpdate.setEmail(patient.getEmail());
        patientToUpdate.setAddress(patient.getAddress());
        patientToUpdate.setDateOfBirth(patient.getDateOfBirth());
        patientToUpdate.setPhoneNumber(patient.getPhoneNumber());
        patientToUpdate.setMBO(patient.getMBO());

        patientRepository.save(patientToUpdate);
        return ResponseEntity.ok().build();
    }

    private static LocalDate getStartOfWeek(LocalDate date) {
        return date.with(DayOfWeek.MONDAY);
    }


    public ResponseEntity<Map<LocalDate, List<PatientSessionsDTO>>> getPatientDashboard() {
        Patient patient = (Patient) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Appointment> appointments = patient.getAppointments();

        // Sort appointments by date-time before processing
        List<Appointment> sortedAppointments = appointments.stream()
                .sorted(Comparator.comparing(Appointment::getDateTime))
                .collect(Collectors.toList());


        Map<LocalDate, List<PatientSessionsDTO>> patientSessionsByWeek = sortedAppointments.stream()
                .map(appointment -> {
                    Therapy therapy = appointment.getTherapy();
                    int numOfSessions = therapy.getAppointments().size();
                    int index = IntStream.range(0, numOfSessions)
                            .filter(i -> therapy.getAppointments().get(i).getId().equals(appointment.getId()))
                            .findFirst().orElseThrow(() -> new RuntimeException("Appointment not found"));
                    return buildPatientSessionsDTO(appointment, sortedAppointments, numOfSessions, index);
                }).sorted(Comparator.comparing(PatientSessionsDTO::getDateTime))
                .collect(Collectors.groupingBy(dto -> getStartOfWeek(dto.getDateTime().toLocalDate()),
                        LinkedHashMap::new, // Use LinkedHashMap to maintain order
                        Collectors.toList()));

        // Sort the DTOs within each week by date-time
        patientSessionsByWeek.forEach((week, sessions) -> sessions.sort(Comparator.comparing(dto -> dto.getDateTime())));

        return ResponseEntity.ok(patientSessionsByWeek);
    }

    private PatientSessionsDTO buildPatientSessionsDTO(Appointment appointment, List<Appointment> appointments, int numOfSessions, int index) {
        return PatientSessionsDTO.builder()
                .equipmentRoomName(appointment.getTherapy().getTherapyType().getRequiredEquipment().getRoomName())
                .appointmentId(appointments.stream().filter(app -> app.getId().equals(appointment.getId())).findFirst().get().getId())
                .dateTime(appointment.getDateTime())
                .sessionNotes(appointment.getSessionNotes())
                .numberOfSessions(numOfSessions)
                .numberOfSessionsCompleted(index + 1)
                .therapyTypeName(appointment.getTherapy().getTherapyType().getName())
                .employeeFirstName(appointment.getEmployee().getFirstName())
                .employeeLastName(appointment.getEmployee().getLastName())
                .build();
    }


    public ResponseEntity<Void> updateProfilePicture(MultipartFile file) {
        Patient patient = (Patient) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        patient.setPhoto(compressPhoto(file));
        patientRepository.save(patient);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    private static byte[] compressPhoto(MultipartFile photo) {
        final long MAX_SIZE = 5 * 1024 * 1024; // 5 MB
        float compressionQuality = photo.getSize() > MAX_SIZE ? 0.5f : 0.75f; // More compression if larger than 5MB

        // Read the MultipartFile into a BufferedImage
        InputStream inputFileStream = photo.getInputStream();
        BufferedImage inputImage = ImageIO.read(inputFileStream);

        // Compress the image
        ByteArrayOutputStream compressedOutputStream = new ByteArrayOutputStream();
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(Objects.requireNonNull(photo.getContentType()).split("/")[1]);
        ImageWriter writer = writers.next();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(compressedOutputStream);
        writer.setOutput(imageOutputStream);
        ImageWriteParam params = writer.getDefaultWriteParam();
        params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        params.setCompressionQuality(compressionQuality); // Adjust the compression quality
        writer.write(null, new IIOImage(inputImage, null, null), params);
        writer.dispose();
        imageOutputStream.close();

        // Convert the compressed image to a byte array
        return compressedOutputStream.toByteArray();
    }
}
