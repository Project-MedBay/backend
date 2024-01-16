package com.medbay.service;

import com.medbay.domain.Appointment;
import com.medbay.domain.DTO.EmployeeStatisticsDTO;
import com.medbay.domain.DTO.EquipmentStatisticsDTO;
import com.medbay.domain.DTO.FacilityDTO;
import com.medbay.domain.Employee;
import com.medbay.domain.Equipment;
import com.medbay.domain.TherapyType;
import com.medbay.domain.enums.ActivityStatus;
import com.medbay.domain.enums.Specialization;
import com.medbay.domain.enums.TherapyStatus;
import com.medbay.domain.request.CreateEquipmentRequest;
import com.medbay.repository.AppointmentRepository;
import com.medbay.repository.EquipmentRepository;
import com.medbay.repository.TherapyTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.medbay.util.Helper.log;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final TherapyTypeRepository therapyTypeRepository;
    private final AppointmentRepository appointmentRepository;

    public ResponseEntity<FacilityDTO> getFacilities() {
        List<Equipment> equipment = equipmentRepository.findAll();
        List<TherapyType> therapyTypes = therapyTypeRepository.findAll();
        List<Specialization> specializations = Arrays.stream(Specialization.values()).toList();
        FacilityDTO facilityDTO = new FacilityDTO(equipment, therapyTypes, specializations);
        return ResponseEntity.ok(facilityDTO);
    }

    public ResponseEntity<Long> createEquipment(CreateEquipmentRequest request) {

        Equipment equipment = Equipment.builder()
                .name(request.getName())
                .capacity(request.getCapacity())
                .description(request.getDescription())
                .roomName(request.getRoomName())
                .specialization(Specialization.valueOf(request.getSpecialization().toUpperCase()))
                .build();

        Equipment savedEquipment = equipmentRepository.save(equipment);
        return ResponseEntity.ok(savedEquipment.getId());
    }

    public ResponseEntity<Void> deleteEquipment(Long id) {
        if (!equipmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        equipmentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<Void> updateEquipment(CreateEquipmentRequest equipment, Long id) {
        Optional<Equipment> equipmentOptional = equipmentRepository.findById(id);
        if (equipmentOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Equipment equipment1 = equipmentOptional.get();
        equipment1.setName(equipment.getName());
        equipment1.setCapacity(equipment.getCapacity());
        equipment1.setDescription(equipment.getDescription());
        equipment1.setRoomName(equipment.getRoomName());
        equipment1.setSpecialization(Specialization.valueOf(equipment.getSpecialization().toUpperCase()));
        equipmentRepository.save(equipment1);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<EquipmentStatisticsDTO> getEquipmentStatistics() {
        List<Equipment> equipments = equipmentRepository.findAll();
        List<Appointment> appointments = appointmentRepository.findAll();

        Map<String, Double> percentageOfHoursWorkedLastMonth = new HashMap<>();
        Map<String, Long> employeesNumberOfAppointments = new HashMap<>();
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        for (Equipment equipment : equipments) {
            String name = equipment.getName();
            long appointmentsHadLastMonth = appointments.stream()
                    .filter(appointment -> appointment.getDateTime().isAfter(thirtyDaysAgo)
                            && appointment.getTherapy().getTherapyStatus().equals(TherapyStatus.VERIFIED)
                            && appointment.getTherapy().getTherapyType().getRequiredEquipment().getName().equals(name))
                    .count();

            long appointmentsCouldHaveHad = 12L * 30 * equipment.getCapacity();
            double percentage = Math.ceil(((double) appointmentsHadLastMonth / appointmentsCouldHaveHad) * 100);
            percentageOfHoursWorkedLastMonth.put(name, percentage);

            long numberOfAppointments = appointments.stream()
                    .filter(appointment -> appointment.getTherapy().getTherapyStatus().equals(TherapyStatus.VERIFIED)
                            && appointment.getDateTime().isBefore(LocalDateTime.now())
                            && appointment.getTherapy().getTherapyType().getRequiredEquipment().getName().equals(name))
                    .map(appointment -> appointment.getTherapy().getPatient().getId())
                    .distinct()
                    .count();

            employeesNumberOfAppointments.put(name, numberOfAppointments);
        }

        employeesNumberOfAppointments = employeesNumberOfAppointments.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        percentageOfHoursWorkedLastMonth = percentageOfHoursWorkedLastMonth.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        EquipmentStatisticsDTO equipmentStatisticsDTO = EquipmentStatisticsDTO.builder()
                .percentageOfHoursWorkedLastMonth(percentageOfHoursWorkedLastMonth)
                .numberOfSessions(employeesNumberOfAppointments)
                .build();

        return ResponseEntity.ok(equipmentStatisticsDTO);

    }
}
