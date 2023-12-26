package com.medbay.controller;

import com.medbay.domain.*;
import com.medbay.domain.request.CreateAppointmentInfoRequest;
import com.medbay.domain.request.CreateAppointmentRequest;
import com.medbay.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {
    private final AppointmentInfoService appointmentInfoService;
    private final AppointmentService appointmentService;
    private final UserService userService;
    private final TherapyService therapyService;
    private final EmployeeService employeeService;
    private final PatientService patientService;
    private final EquipmentService equipmentService;
    private final TherapyTypeService therapyTypeService;
    private final FacilityDataService facilityDataService;

    @GetMapping("/calendar")
    public ResponseEntity<List<Integer>> getNumAppointmentsPerDay(@RequestBody CreateAppointmentInfoRequest request) {
        return ResponseEntity.ok(appointmentInfoService.getNumAppointmentsPerDay(request));
    }

    @GetMapping("/calendar/day")
    public ResponseEntity<List<Appointment>> getAppointmentsPerTimeSlot(@RequestBody CreateAppointmentRequest request) {
        return appointmentService.getAppointmentsPerTimeSlot(request.getDateTime());
    }

    @GetMapping("/verifications/users")
    public ResponseEntity<List<Patient>> getRegistrationsRequests() {
        return patientService.getPendingPatients();
    }

    @GetMapping("/verifications/therapies")
    public ResponseEntity<List<Therapy>> getTherapyRequests() {
        return therapyService.getTherapyRequests();
    }

    @GetMapping("manage/therapists")
    public ResponseEntity<List<Employee>> getTherapists() {
        return employeeService.getEmployees();
    }

    @GetMapping("manage/patients")
    public ResponseEntity<List<Patient>> getPatients() {
        return patientService.getPatients();
    }

//    @GetMapping("/manage/facility")
//    public ResponseEntity<List<Equipment>> getResources() {
//        return equipmentService.getEquipment();
//    }
//    @GetMapping
//    public ResponseEntity<List<TherapyType>> getTherapies() {
//        return  therapyTypeService.getTherapyType();
//    }
    @GetMapping("/manage/facility")
    public ResponseEntity<FacilityData> getFacilityData() {
        return facilityDataService.getFacilityData();
    }


}
