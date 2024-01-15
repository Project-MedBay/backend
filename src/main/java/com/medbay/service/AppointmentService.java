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
                log("Available equipment: " + availableEquipment);
                log("Available employees: " + availableEmployees);
                log("Appointments count: " + appointmentsCount);

                int slotsAvailable = Math.min(availableEquipment, availableEmployees) - appointmentsCount;
                log("Slots available: " + slotsAvailable);
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
        LocalDateTime maxDateTime = patientAppointments.get(0).getDateTime().plusDays(5L * numOfSessions);
        long days = Duration.between(appointment.getDateTime(), maxDateTime).toDays();

        Map<String, List<Integer>> availability = new LinkedHashMap<>();
        for (long day = 0; day < days; day++) {
            LocalDate date = patientAppointments.get(0).getDateTime().toLocalDate().plusDays(day);

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
                log("Available equipment: " + availableEquipment);
                log("Available employees: " + availableEmployees);
                log("Appointments count: " + appointmentsCount);

                boolean overlap = false;
                for (Appointment patientAppointment : patientAppointments) {
                    if (!patientAppointment.getId().equals(appointment.getId())) {
                        LocalDateTime existingAppointmentStart = patientAppointment.getDateTime().minusHours(23);
                        LocalDateTime existingAppointmentEnd = patientAppointment.getDateTime().plusHours(23);
                        if ((dateTime.isAfter(existingAppointmentStart) || dateTime.isEqual(existingAppointmentStart)) &&
                                (dateTime.isBefore(existingAppointmentEnd))) {
                            log("Overlap within 24-hour period of appointment at: " + patientAppointment.getDateTime());
                            overlap = true;
                            break;
                        }
                    }
                }

                int slotsAvailable = Math.min(availableEquipment, availableEmployees) - appointmentsCount;
                log("Slots available: " + slotsAvailable);
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
