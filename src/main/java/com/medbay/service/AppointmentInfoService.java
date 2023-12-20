package com.medbay.service;

import com.medbay.domain.AppointmentInfo;
import com.medbay.domain.Equipment;
import com.medbay.domain.request.CreateAppointmentInfoRequest;
import com.medbay.repository.AppointmentInfoRepository;
import com.medbay.repository.EmployeeRepository;
import com.medbay.repository.EquipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentInfoService {

    private final AppointmentInfoRepository appointmentInfoRepository;
    private final EmployeeRepository employeeRepository;
    private final EquipmentRepository equipmentRepository;

    @Transactional(readOnly = true)
    public List<AppointmentInfo> getAppointmentInfo(LocalDate selectedDate) {
        return appointmentInfoRepository.findByDate(selectedDate);
    }

    @Transactional
    public ResponseEntity<Void> createAppointmentInfo(CreateAppointmentInfoRequest request) {
        LocalDate selectedDate = request.getAppointmentDate();

        List<AppointmentInfo> generatedSchedule = generateAppointmentInfo(selectedDate, request.getEquipmentId());


        appointmentInfoRepository.saveAll(generatedSchedule);

        return ResponseEntity.ok().build();
    }


    @Transactional
    public ResponseEntity<Void> deleteAppointmentInfo(Long id) {

        AppointmentInfo appointmentInfo = appointmentInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AppointmentInfo not found with id: " + id));

        // employeeRepository.updateEmployeeAvailability(appointmentInfo.getEmployee().getId(), 1);
        // equipmentRepository.updateEquipmentAvailability(appointmentInfo.getEquipment().getId(), 1);

        appointmentInfoRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }

    public List<AppointmentInfo> generateAppointmentInfo(LocalDate selectedDate, long equipmentId) {
        List<AppointmentInfo> appointmentInfoList = new ArrayList<>();

        LocalDateTime startDateTime = LocalDateTime.of(selectedDate, LocalTime.of(8, 0));
        LocalDateTime endDateTime;

        while (startDateTime.getHour() < 20) {
            endDateTime = startDateTime.plusMinutes(45);

            // Stvaranje i dodavanje AppointmentInfo objekta u listu
            AppointmentInfo appointmentInfo = new AppointmentInfo();
            appointmentInfo.setAppointmentDate(startDateTime);
            appointmentInfo.setEmployeeCapacity(employeeRepository.employeeCapacity());
            Equipment equipment = equipmentRepository.findById(equipmentId)
                    .orElseThrow(() -> new EntityNotFoundException("Equipment not found with id: " + equipmentId));

            appointmentInfo.setEquipmentCapacity(equipment.getCapacity());
            appointmentInfo.setEmployeeCapacity(employeeRepository.employeeCapacity());
            appointmentInfoList.add(appointmentInfo);

            startDateTime = endDateTime;
        }

        return appointmentInfoList;
    }

    public List<AppointmentInfo> getAppointmentInfoForMonth(LocalDate startDate, LocalDate endDate, Long equipmentId) {
        List<AppointmentInfo> appointmentInfoList = new ArrayList<>();

        while (!startDate.isAfter(endDate)) {
            List<AppointmentInfo> dailyAppointments = getExistingAppointmentsOrGenerate(startDate, equipmentId);
            appointmentInfoList.addAll(dailyAppointments);

            startDate = startDate.plusDays(1);
        }

        return appointmentInfoList;
    }

    private List<AppointmentInfo> getExistingAppointmentsOrGenerate(LocalDate date, Long equipmentId) {
        List<AppointmentInfo> existingAppointments = appointmentInfoRepository.findByDate(date);

        if (existingAppointments.isEmpty()) {
            // Ako ne postoje termini za taj datum, generiraj ih
            return generateAppointmentInfo(date, equipmentId);
        } else {
            // Ako postoje, vrati ih
            return existingAppointments;
        }
    }



}
