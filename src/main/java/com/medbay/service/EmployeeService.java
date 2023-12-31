package com.medbay.service;

import com.medbay.domain.DTO.EmployeeSessionsDTO;
import com.medbay.domain.*;
import com.medbay.domain.DTO.PatientDTO;
import com.medbay.domain.enums.ActivityStatus;
import com.medbay.domain.enums.Role;
import com.medbay.domain.enums.Specialization;
import com.medbay.domain.request.CreateEmployeeRequest;
import com.medbay.repository.AppointmentRepository;
import com.medbay.repository.EmployeeRepository;
import com.medbay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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


    public ResponseEntity<Void> createEmployee(CreateEmployeeRequest request) {

        if(userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }


        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(ActivityStatus.ACTIVE)
                .role(Role.STAFF)
                .specialization(Specialization.valueOf(request.getSpecialization().toUpperCase()))
                .build();

        employeeRepository.save(employee);
        return ResponseEntity.ok().build();
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

    public ResponseEntity<Map<LocalDate, List<EmployeeSessionsDTO>>> getEmployeesAppointments() {
        Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Appointment> appointments = appointmentRepository.findByEmployeeAndDateTimeAfter(employee, LocalDateTime.now());

        Map<LocalDate, List<EmployeeSessionsDTO>> employeeSessionsByWeek = appointments.stream()
                .map(appointment -> {
                    Therapy therapy = appointment.getTherapy();
                    int numOfSessions = therapy.getAppointments().size();
                    int index = IntStream.range(0, numOfSessions)
                            .filter(i -> therapy.getAppointments().get(i).getId().equals(appointment.getId()))
                            .findFirst().orElseThrow(() -> new RuntimeException("Appointment not found"));

                    return buildEmployeeSessionsDTO(appointment, appointments, numOfSessions, index);
                })
                .collect(Collectors.groupingBy(dto -> getStartOfWeek(dto.getDateTime().toLocalDate())));

        return ResponseEntity.ok(employeeSessionsByWeek);
    }

    private static EmployeeSessionsDTO buildEmployeeSessionsDTO(Appointment appointment, List<Appointment> appointments,
                                                                int numOfSessions, int index) {
        return EmployeeSessionsDTO.builder()
                .id(appointments.indexOf(appointment) + 1)
                .sessionNotes(appointment.getSessionNotes())
                .therapyTypeName(appointment.getTherapy().getTherapyType().getName())
                .dateTime(appointment.getDateTime())
                .equipmentRoomName(appointment.getTherapy().getTherapyType().getRequiredEquipment().getRoomName())
                .numberOfSessions(numOfSessions + 1)
                .numberOfSessionsCompleted(index + 1)
                .patient(buildPatientDTO(appointment))
                .build();
    }

    private static PatientDTO buildPatientDTO(Appointment appointment) {
        return PatientDTO.builder()
                .id(appointment.getPatient().getId())
                .OIB(appointment.getPatient().getOIB())
                .MBO(appointment.getPatient().getMBO())
                .firstName(appointment.getPatient().getFirstName())
                .lastName(appointment.getPatient().getLastName()).email(appointment.getPatient().getEmail())
                .phoneNumber(appointment.getPatient().getPhoneNumber())
                .address(appointment.getPatient().getAddress())
                .createdAt(appointment.getPatient().getCreatedAt())
                .dateOfBirth(appointment.getPatient().getDateOfBirth())
                .photo(appointment.getPatient().getPhoto())
                .build();
    }


}
