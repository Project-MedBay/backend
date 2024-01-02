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

import java.lang.reflect.Array;
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
    public List<AppointmentInfo> getAppointmentInfo(CreateAppointmentInfoRequest request) {
        List<AppointmentInfo> foundAppointmentInfo = appointmentInfoRepository.findByAppointmentDate(request.getAppointmentDate());
        if (foundAppointmentInfo.isEmpty()) {
            foundAppointmentInfo = generateAppointmentInfo(request.getAppointmentDate(), request.getEquipmentId());
        }
        return foundAppointmentInfo;
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
//nisam sigurna je li nam potreban delete jer nikad necemo brisat datume i te kombinacije iz baze, nego cemo prilikom prijave terapije samo smanjivat capacity
        // AppointmentInfo appointmentInfo = appointmentInfoRepository.findById(id)
        //       .orElseThrow(() -> new EntityNotFoundException("AppointmentInfo not found with id: " + id));

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
            endDateTime = startDateTime.plusMinutes(60);

            Equipment equipment = equipmentRepository.findById(equipmentId)
                    .orElseThrow(() -> new EntityNotFoundException("Equipment not found with id: " + equipmentId));

            AppointmentInfo appointmentInfo = AppointmentInfo.builder()
                    .appointmentDate(startDateTime)
                    .equipmentCapacity(equipment.getCapacity())
                    .employeeCapacity(employeeRepository.employeeCapacity())
                    .build();

            appointmentInfoList.add(appointmentInfo);

            startDateTime = endDateTime;
        }

        return appointmentInfoList;
    }
}
