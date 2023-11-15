package com.medbay.controller;

import com.medbay.domain.Employee;
import com.medbay.domain.request.CreateEmployeeRequest;
import com.medbay.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees(){
        return employeeService.getEmployees();
    }
    @PostMapping
    public ResponseEntity<Void> createEmployee(@RequestBody CreateEmployeeRequest employee){
        return employeeService.createEmployee(employee);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateEmployee(@PathVariable("id") Long id){
        return employeeService.deactivateEmployee(id);
    }
}
