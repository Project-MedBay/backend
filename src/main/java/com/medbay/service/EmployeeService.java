package com.medbay.service;

import com.medbay.DTO.EmployeeFrontDTO;
import com.medbay.DTO.ExtendedAppointmentDTO;
import com.medbay.domain.*;
import com.medbay.domain.Employee;
import com.medbay.domain.Therapy;
import com.medbay.domain.enums.ActivityStatus;
import com.medbay.domain.enums.Role;
import com.medbay.domain.enums.Specialization;
import com.medbay.domain.request.CreateEmployeeRequest;
import com.medbay.repository.EmployeeRepository;
import com.medbay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AppointmentService appointmentService;
    private final TherapyService therapyService;
    private final EquipmentService equipmentService;
    private final TherapyTypeService therapyTypeService;
    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return ResponseEntity.ok(employees);
    }
    public void addTherapy(Therapy therapy, Long employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();


            if (employee.getTherapies().isEmpty()) {
                employee.setTherapies(new ArrayList<>());
            }
            employee.getTherapies().add(therapy);
            therapy.setEmployee(employee);

            // Sačuvaj ažuriranog zaposlenog sa dodatom terapijom
            employeeRepository.save(employee);

        }
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
                .role(Role.ROLE_STAFF)
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

    public Employee getEmployeeById(Long employeeId) {

        return employeeRepository.findById(employeeId).orElse(new Employee());
    }

    public ResponseEntity<List<EmployeeFrontDTO>> getAppointmentsForEmployee(Employee employee) {
        ResponseEntity<List<Appointment>> response = appointmentService.getAppointmentsFromEmployee(employee);
        List<Appointment> appointments = response.getBody();
        if (appointments == null || appointments.isEmpty()) {
            return null;
        }
        List<EmployeeFrontDTO> appointmentDetails = appointments.stream().map(appointment -> {
            Therapy therapy = therapyService.findById(appointment.getTherapy().getId());
            TherapyType therapyType = therapyTypeService.findById(therapy.getTherapyType().getId());
            Equipment equipment = equipmentService.findById(therapyType.getRequiredEquipment().getId());

            return new EmployeeFrontDTO(appointment, equipment);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(appointmentDetails);
    }

    public ResponseEntity<ExtendedAppointmentDTO> getAppointmentDetails(Employee employee, Long appointmentId) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);

        Therapy therapy = therapyService.findById(appointment.getTherapy().getId());
        TherapyType therapyType = therapyTypeService.findById(therapy.getTherapyType().getId());
        Equipment equipment = equipmentService.findById(therapyType.getRequiredEquipment().getId());

        Patient patient = appointment.getPatient();

        ExtendedAppointmentDTO extendedAppointmentDTO = new ExtendedAppointmentDTO(
                appointment.getDateTime(),
                equipment,
                patient,
                therapyType.getDescription()
        );

        return ResponseEntity.ok(extendedAppointmentDTO);
    }
}
