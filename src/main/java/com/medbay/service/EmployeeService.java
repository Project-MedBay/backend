package com.medbay.service;

import com.medbay.domain.Employee;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> employees = employeeRepository.findAll();
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
}
