package com.medbay.service;

import com.medbay.domain.Employee;
import com.medbay.domain.User;
import com.medbay.domain.enums.Role;
import com.medbay.domain.request.CreateEmployeeRequest;
import com.medbay.repository.EmployeeRepository;
import com.medbay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Employee employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .role(Role.ROLE_STAFF)
                .specialization(request.getSpecialization())
                .build();

        employeeRepository.save(employee);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        employeeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
