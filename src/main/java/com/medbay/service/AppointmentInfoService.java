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
    public ResponseEntity<List<AppointmentInfo>> getAppointmentInfo() {
        List<AppointmentInfo> appointmentInfos = appointmentInfoRepository.findAll();
        return ResponseEntity.ok(appointmentInfos);
    }

    @Transactional
    public ResponseEntity<Void> createAppointmentInfo(CreateAppointmentInfoRequest request) {
        LocalDate selectedDate = request.getAppointmentDate();

        List<AppointmentInfo> generatedSchedule = generateAppointmentInfo(selectedDate);

        for (AppointmentInfo appointmentInfo : generatedSchedule) {
            Equipment equipment = equipmentRepository.findById(request.getEquipmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Equipment not found with id: " + request.getEquipmentId()));

            appointmentInfo.setEquipmentCapacity(equipment.getCapacity());
            appointmentInfo.setEmployeeCapacity(employeeRepository.employeeCapacity());
        }

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

    private List<AppointmentInfo> generateAppointmentInfo(LocalDate selectedDate) {
        List<AppointmentInfo> appointmentInfoList = new ArrayList<>();

        LocalDateTime startDateTime = LocalDateTime.of(selectedDate, LocalTime.of(8, 0));
        LocalDateTime endDateTime;

        while (startDateTime.getHour() < 16) {
            endDateTime = startDateTime.plusMinutes(45);

            // Stvaranje i dodavanje AppointmentInfo objekta u listu
            AppointmentInfo appointmentInfo = new AppointmentInfo();
            appointmentInfo.setAppointmentDate(startDateTime);

            appointmentInfoList.add(appointmentInfo);

            startDateTime = endDateTime;
        }

        return appointmentInfoList;
    }

}
