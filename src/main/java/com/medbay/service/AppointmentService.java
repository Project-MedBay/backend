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
import java.time.LocalDate;
import java.time.LocalDateTime;
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


    public ResponseEntity<Map<LocalDate, List<Integer>>> getAvailability(String therapyCode, int days) {
        TherapyType therapyType = therapyTypeRepository.findByTherapyCode(therapyCode)
                .orElseThrow(() -> new EntityNotFoundException("Therapy type with code " + therapyCode + " not found"));
        Equipment equipment = therapyType.getRequiredEquipment();

        LocalDate start = LocalDate.now();

        Map<LocalDate, List<Integer>> availability = IntStream.range(0, days)
                .mapToObj(start::plusDays)
                .filter(date -> !(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY))
                .collect(Collectors.toMap(
                        date -> date,
                        date -> calculateDailyAvailability(date, therapyType, equipment),
                        (u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); },
                        LinkedHashMap::new
                ));

        return ResponseEntity.ok(availability);
    }

    private List<Integer> calculateDailyAvailability(LocalDate date, TherapyType therapyType, Equipment equipment) {
        return IntStream.rangeClosed(8, 19) // Assuming the working hours are from 8 to 19 inclusive.
                .filter(hour -> {
                    LocalDateTime dateTime = date.atTime(hour, 0);
                    int appointmentsCount = appointmentRepository.countAppointmentsByTherapyTypeAndDateTime(therapyType, dateTime);
                    int availableEquipment = equipment.getCapacity();
                    int availableEmployees = employeeRepository.countBySpecialization(equipment.getSpecialization());
                    return Math.max(Math.min(availableEquipment, availableEmployees) - appointmentsCount, 0) > 0;
                })
                .boxed() // Box the int values to Integers, so they can be collected to a List<Integer>.
                .collect(Collectors.toList());
    }



    public ResponseEntity<Void> updateAppointmentSessionNotes(Long appointmentId, String sessionNotes) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new EntityNotFoundException("Appointment not found"));
        appointment.setSessionNotes(sessionNotes);
        appointmentRepository.save(appointment);
        return ResponseEntity.ok().build();
    }

}
