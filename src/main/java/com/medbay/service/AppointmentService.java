package com.medbay.service;

import com.medbay.domain.Appointment;
import com.medbay.domain.DTO.AdminCalendarDTO;
import com.medbay.domain.Equipment;
import com.medbay.domain.TherapyType;
import com.medbay.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.medbay.util.Helper.log;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final EmployeeRepository employeeRepository;
    private final TherapyTypeRepository therapyTypeRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");



    public ResponseEntity<Map<LocalDateTime, List<AdminCalendarDTO>>> getAppointmentsForWeek(LocalDate date) {
        if (date.getDayOfWeek() != DayOfWeek.MONDAY) {
            return ResponseEntity.badRequest().build();
        }

        Map<LocalDateTime, List<AdminCalendarDTO>> appointmentsPerHour = new LinkedHashMap<>();

        // Loop over the 5 workdays of the week
        IntStream.range(0, 5).forEach(dayOffset -> {
            LocalDate currentDate = date.plusDays(dayOffset);
            // Loop over the working hours of each day
            IntStream.rangeClosed(8, 19).forEach(hour -> {
                LocalDateTime dateTime = currentDate.atTime(hour, 0);
                List<Appointment> appointments = appointmentRepository.findByDateTime(dateTime);

                // Map appointments to AdminCalendarDTO and collect to list
                List<AdminCalendarDTO> dtos = appointments.stream()
                        .map(this::createAdminCalendarDTO)
                        .collect(Collectors.toList());

                appointmentsPerHour.put(dateTime, dtos);
            });
        });

        return ResponseEntity.ok(appointmentsPerHour);
    }

    private AdminCalendarDTO createAdminCalendarDTO(Appointment appointment) {
        return AdminCalendarDTO.builder()
                .appointmentId(appointment.getId())
                .therapyId(appointment.getTherapy().getId())
                .patientFirstName(appointment.getTherapy().getPatient().getFirstName())
                .patientLastName(appointment.getTherapy().getPatient().getLastName())
                .employeeFirstName(appointment.getEmployee().getFirstName())
                .employeeLastName(appointment.getEmployee().getLastName())
                .therapyTypeName(appointment.getTherapy().getTherapyType().getName())
                .build();
    }


    public ResponseEntity<Map<String, List<Integer>>> getAvailability(String therapyCode, int days) {
        Map<String, List<Integer>> availability = new LinkedHashMap<>();
        TherapyType therapyType = therapyTypeRepository.findByTherapyCode(therapyCode)
                .orElseThrow(() -> new EntityNotFoundException("Therapy type with code " + therapyCode + " not found"));

        Equipment equipment = therapyType.getRequiredEquipment();

        LocalDate today = LocalDate.now();
        for (int day = 0; day < days; day++) {
            LocalDate date = today.plusDays(day);

            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                availability.put(date.format(DATE_FORMATTER), new ArrayList<>());
                continue;
            }

            List<Integer> availableHours = new ArrayList<>();
            for (int hour = 8; hour <= 19; hour++) {
                LocalDateTime dateTime = date.atTime(hour, 0);

                int appointmentsCount = appointmentRepository.countAppointmentsByTherapyTypeAndDateTime(therapyType, dateTime);
                int availableEquipment = equipment.getCapacity();
                int availableEmployees = employeeRepository.countBySpecialization(equipment.getSpecialization());


                int slotsAvailable = Math.min(availableEquipment, availableEmployees) - appointmentsCount;
                if (slotsAvailable < 0) {
                    throw new RuntimeException("Error: Negative slots available");
                }
                else if(slotsAvailable > 0) {
                    availableHours.add(hour);
                }
            }

            availability.put(date.format(DATE_FORMATTER), availableHours);
        }

        return ResponseEntity.ok(availability);
    }



    public ResponseEntity<Void> updateAppointmentSessionNotes(Long appointmentId, String sessionNotes) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
        appointment.setSessionNotes(sessionNotes);
        appointmentRepository.save(appointment);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Map<String, List<Integer>>> getAvailabilityForReschedule(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        TherapyType therapyType = appointment.getTherapy().getTherapyType();
        Equipment equipment = therapyType.getRequiredEquipment();

        List<Appointment> patientAppointments = appointment.getTherapy().getAppointments()
                .stream().sorted(Comparator.comparing(Appointment::getDateTime)).toList();

        int numOfSessions = patientAppointments.size();
        LocalDate dateNow = LocalDate.now().plusDays(2);
        LocalDate maxDateTime = patientAppointments.get(0).getDateTime().toLocalDate().plusDays(5L * numOfSessions);
        long days = ChronoUnit.DAYS.between(dateNow, maxDateTime);


        LocalDate date;
        Map<String, List<Integer>> availability = new LinkedHashMap<>();
        for (long day = 0; day < days; day++) {
            date = dateNow.plusDays(day);

            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                availability.put(date.format(DATE_FORMATTER), new ArrayList<>());
                continue;
            }

            List<Integer> availableHours = new ArrayList<>();
            for (int hour = 8; hour <= 19; hour++) {
                LocalDateTime dateTime = date.atTime(hour, 0);

                int appointmentsCount = appointmentRepository.countAppointmentsByTherapyTypeAndDateTime(therapyType, dateTime);
                int availableEquipment = equipment.getCapacity();
                int availableEmployees = employeeRepository.countBySpecialization(equipment.getSpecialization());

                boolean overlap = false;
                for (Appointment patientAppointment : patientAppointments) {
                    if (!patientAppointment.getId().equals(appointment.getId())) {
                        long hoursDifference = Duration.between(patientAppointment.getDateTime(), dateTime).abs().toHours();
                        if (hoursDifference < 24) {
                            overlap = true;
                            break;
                        }
                    }
                }

                int slotsAvailable = Math.min(availableEquipment, availableEmployees) - appointmentsCount;
                if (slotsAvailable < 0) {
                    throw new RuntimeException("Error: Negative slots available");
                }
                else if(slotsAvailable > 0 && !overlap) {
                    availableHours.add(hour);
                }
            }

            availability.put(date.format(DATE_FORMATTER), availableHours);
        }

        return ResponseEntity.ok(availability);
    }


    public ResponseEntity<Void> rescheduleAppointment(Long appointmentId, LocalDateTime newDateTime) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));

        appointment.setDateTime(newDateTime);
        appointmentRepository.save(appointment);
        return ResponseEntity.ok().build();
    }
}
