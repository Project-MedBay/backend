package com.medbay.service;

import com.medbay.domain.DTO.AppointmentDTO;
import com.medbay.domain.DTO.EmployeeSessionsDTO;
import com.medbay.domain.*;
import com.medbay.domain.DTO.EmployeeStatisticsDTO;
import com.medbay.domain.DTO.PatientDTO;
import com.medbay.domain.enums.ActivityStatus;
import com.medbay.domain.enums.Role;
import com.medbay.domain.enums.Specialization;
import com.medbay.domain.enums.TherapyStatus;
import com.medbay.domain.request.CreateEmployeeRequest;
import com.medbay.repository.AppointmentRepository;
import com.medbay.repository.EmployeeRepository;
import com.medbay.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.medbay.util.Helper.log;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AppointmentRepository appointmentRepository;

    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> employees = employeeRepository.findAllByStatus(ActivityStatus.ACTIVE);
        return ResponseEntity.ok(employees);
    }


    public ResponseEntity<Long> createEmployee(CreateEmployeeRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .createdAt(request.getCreatedAt().atStartOfDay())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(ActivityStatus.ACTIVE)
                .role(Role.STAFF)
                .specialization(Specialization.valueOf(request.getSpecialization().toUpperCase()))
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(savedEmployee.getId());
    }


    public ResponseEntity<Void> deactivateEmployee(Long id) {

        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        employee.get().setStatus(ActivityStatus.DEACTIVATED);
        employeeRepository.save(employee.get());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<Patient>> getPatientsFromEmployee(Employee employee) {
        List<Appointment> appointments = appointmentRepository.findByEmployee(employee);

        List<Patient> patients = appointments.stream()
                .map(Appointment::getPatient)
                .distinct()
                .collect(Collectors.toList());

        return ResponseEntity.ok(patients);
    }


    private static LocalDate getStartOfWeek(LocalDate date) {
        return date.with(DayOfWeek.MONDAY);
    }

    @Transactional
    public ResponseEntity<Map<LocalDate, List<EmployeeSessionsDTO>>> getEmployeesAppointments() {
        Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Appointment> appointments = employee.getAppointments();

        // Sort appointments by date-time before processing
        List<Appointment> sortedAppointments = appointments.stream()
                .sorted(Comparator.comparing(Appointment::getDateTime))
                .collect(Collectors.toList());

        // Group by start of the week and map to DTOs
        Map<LocalDate, List<EmployeeSessionsDTO>> employeeSessionsByWeek = sortedAppointments.stream()
                .map(appointment -> {
                    Therapy therapy = appointment.getTherapy();
                    int numOfSessions = therapy.getAppointments().size();
                    int index = IntStream.range(0, numOfSessions)
                            .filter(i -> therapy.getAppointments().get(i).getId().equals(appointment.getId()))
                            .findFirst().orElseThrow(() -> new RuntimeException("Appointment not found"));
                    return buildEmployeeSessionsDTO(appointment, sortedAppointments, numOfSessions, index);
                }).sorted(Comparator.comparing(EmployeeSessionsDTO::getDateTime))
                .collect(Collectors.groupingBy(dto -> getStartOfWeek(dto.getDateTime().toLocalDate()),
                        LinkedHashMap::new, // Use LinkedHashMap to maintain order
                        Collectors.toList()));

        // Sort the DTOs within each week by date-time
        employeeSessionsByWeek.forEach((week, sessions) -> sessions.sort(Comparator.comparing(dto -> dto.getDateTime())));

        return ResponseEntity.ok(employeeSessionsByWeek);
    }


    private static EmployeeSessionsDTO buildEmployeeSessionsDTO(Appointment appointment, List<Appointment> appointments,
                                                                int numOfSessions, int index) {
        Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return EmployeeSessionsDTO.builder()
                .appointmentId(appointment.getId())
                .id(appointments.indexOf(appointment) + 1)
                .sessionNotes(appointment.getSessionNotes())
                .therapyTypeName(appointment.getTherapy().getTherapyType().getName())
                .dateTime(appointment.getDateTime())
                .equipmentRoomName(appointment.getTherapy().getTherapyType().getRequiredEquipment().getRoomName())
                .numberOfSessions(numOfSessions + 1)
                .numberOfSessionsCompleted(index + 1)
                .patient(buildPatientDTO(appointment, employee))
                .build();
    }

    private static PatientDTO buildPatientDTO(Appointment appointment, Employee employee) {
        return PatientDTO.builder()
                .id(appointment.getPatient().getId())
                .MBO(appointment.getPatient().getMBO())
                .firstName(appointment.getPatient().getFirstName())
                .lastName(appointment.getPatient().getLastName()).email(appointment.getPatient().getEmail())
                .phoneNumber(appointment.getPatient().getPhoneNumber())
                .address(appointment.getPatient().getAddress())
                .createdAt(appointment.getPatient().getCreatedAt())
                .dateOfBirth(appointment.getPatient().getDateOfBirth())
                .photo(appointment.getPatient().getPhoto())
                .appointments(appointment.getPatient().getAppointments().stream()
                        .map(app -> AppointmentDTO.builder()
                                .appointmentId(app.getId())
                                .appointmentDate(app.getDateTime())
                                .therapyName(app.getTherapy().getTherapyType().getName())
                                .sessionNotes(app.getSessionNotes())
                                .show(employee.getId().equals(app.getEmployee().getId()))
                                .build()).sorted(Comparator.comparing(AppointmentDTO::getAppointmentDate))
                        .collect(Collectors.toList()))
                .build();
    }


    public ResponseEntity<Void> updateEmployee(CreateEmployeeRequest employee, Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Employee employeeToUpdate = employeeOptional.get();
        employeeToUpdate.setFirstName(employee.getFirstName());
        employeeToUpdate.setLastName(employee.getLastName());
        employeeToUpdate.setEmail(employee.getEmail());
        employeeToUpdate.setSpecialization(Specialization.valueOf(employee.getSpecialization().toUpperCase()));

        employeeRepository.save(employeeToUpdate);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<EmployeeStatisticsDTO> getEmployeeStatistics() {
        List<Employee> employees = employeeRepository.findAllByStatus(ActivityStatus.ACTIVE);

        Map<String, Double> percentageOfHoursWorkedLastMonth = new HashMap<>();
        Map<String, Long> employeesNumberOfAppointments = new HashMap<>();
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().atZone(ZoneId.of("UTC+1")).toLocalDateTime().minusDays(30);

        for (Employee employee : employees) {
            String fullname = employee.getFirstName() + " " + employee.getLastName();
            long appointmentsHadLastMonth = employee.getAppointments().stream()
                    .filter(appointment -> appointment.getDateTime().isAfter(thirtyDaysAgo)
                                        && appointment.getTherapy().getTherapyStatus().equals(TherapyStatus.VERIFIED))
                    .count();

            long appointmentsCouldHaveHad = 0;
            if(employee.getCreatedAt().isBefore(thirtyDaysAgo)) {
                appointmentsCouldHaveHad += 8 * 30;
            } else {
                appointmentsCouldHaveHad += 8 * Duration.between(employee.getCreatedAt(), LocalDateTime.now()).toDays();
            }

            double percentage = Math.ceil(((double) appointmentsHadLastMonth / appointmentsCouldHaveHad) * 100);
            percentageOfHoursWorkedLastMonth.put(fullname, percentage);

            long numberOfAppointments = employee.getAppointments().stream()
                    .filter(appointment -> appointment.getTherapy().getTherapyStatus().equals(TherapyStatus.VERIFIED)
                                        && appointment.getDateTime().isBefore(LocalDateTime.now().atZone(ZoneId.of("UTC+1")).toLocalDateTime()))
                    .map(appointment -> appointment.getTherapy().getPatient().getId())
                    .distinct()
                    .count();

            employeesNumberOfAppointments.put(fullname, numberOfAppointments);
        }

        employeesNumberOfAppointments = employeesNumberOfAppointments.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        percentageOfHoursWorkedLastMonth = percentageOfHoursWorkedLastMonth.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        EmployeeStatisticsDTO employeeStatisticsDTO = EmployeeStatisticsDTO.builder()
                .percentageOfHoursWorkedLastMonth(percentageOfHoursWorkedLastMonth)
                .numberOfSessions(employeesNumberOfAppointments)
                .build();

        return ResponseEntity.ok(employeeStatisticsDTO);
    }
}
