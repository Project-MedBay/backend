package com.medbay.controller;

import com.medbay.DTO.EmployeeFrontDTO;
import com.medbay.DTO.ExtendedAppointmentDTO;
import com.medbay.domain.Employee;
import com.medbay.domain.Patient;
import com.medbay.domain.request.CreateEmployeeRequest;
import com.medbay.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.medbay.domain.*;
import com.medbay.domain.request.CreateAppointmentInfoRequest;
import com.medbay.domain.request.CreateAppointmentRequest;
import com.medbay.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EmployeeController {

    private final AppointmentInfoService appointmentInfoService;
    private final AppointmentService appointmentService;
    private final UserService userService;
    private final TherapyService therapyService;
    private final EmployeeService employeeService;
    private final PatientService patientService;
    private final EquipmentService equipmentService;
    private final TherapyTypeService therapyTypeService;





    @GetMapping("/{employeeId}")
    public ResponseEntity<List<EmployeeFrontDTO>> getAppointmentsForEmployee(
            @PathVariable Long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);

        return employeeService.getAppointmentsForEmployee(employee);

    }

    @GetMapping("/{employeeId}/appointment/{appointmentId}")
    public ResponseEntity<ExtendedAppointmentDTO> getAppointmentDetails(
            @PathVariable Long employeeId,
            @PathVariable Long appointmentId) {
        Employee employee = employeeService.getEmployeeById(employeeId);

        return employeeService.getAppointmentDetails(employee, appointmentId);



    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getPatients(@RequestParam Employee employee) {
        return patientService.getPatientsFromEmployee(employee);
    }
    //@GetMapping("/sessions")          zakljucujem da gumb sessions zapravo treba raditi get appointments for employee pa komentiram ovo
    //public ResponseEntity<List<Appointment>> getSessions(@RequestParam Employee employee) {
    //    return appointmentService.getAppointmentsFromEmployee(employee);
    //}



    @PutMapping("/{appointmentId}/reschedule")
    public ResponseEntity<Void> rescheduleAppointment(
            @PathVariable Long appointmentId,
            @RequestBody LocalDateTime newDateTime) {

        appointmentService.rescheduleAppointment(appointmentId, newDateTime);
        return ResponseEntity.ok().build();
    }


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

    @GetMapping("/manage/therapists")
    public ResponseEntity<List<Employee>> getTherapists() {
        return employeeService.getEmployees();
    }

}
