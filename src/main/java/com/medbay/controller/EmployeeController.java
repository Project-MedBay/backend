package com.medbay.controller;

import com.medbay.domain.DTO.EmployeeSessionsDTO;
import com.medbay.domain.DTO.EmployeeStatisticsDTO;
import com.medbay.domain.Employee;
import com.medbay.domain.Patient;
import com.medbay.domain.request.CreateEmployeeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.medbay.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/appointments")
    public ResponseEntity<Map<LocalDate, List<EmployeeSessionsDTO>>> getAppointmentsForEmployee() {
        return employeeService.getEmployeesAppointments();
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getPatients(@RequestBody Employee employee) {
        return employeeService.getPatientsFromEmployee(employee);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees(){
        return employeeService.getEmployees();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEmployee(@PathVariable("id") Long id,
                                               @RequestBody CreateEmployeeRequest employee){
        return employeeService.updateEmployee(employee, id);
    }

    @PostMapping
    public ResponseEntity<Long> createEmployee(@RequestBody CreateEmployeeRequest employee){
        return employeeService.createEmployee(employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateEmployee(@PathVariable("id") Long id){
        return employeeService.deactivateEmployee(id);
    }

    @GetMapping("/statistics")
    public ResponseEntity<EmployeeStatisticsDTO> getEmployeeStatistics(){
        return employeeService.getEmployeeStatistics();
    }


}
