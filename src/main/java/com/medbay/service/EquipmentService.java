package com.medbay.service;

import com.medbay.domain.Appointment;
import com.medbay.domain.Equipment;
import com.medbay.domain.request.CreateEquipmentRequest;
import com.medbay.repository.AppointmentRepository;
import com.medbay.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final AppointmentRepository appointmentRepository;

    public ResponseEntity<List<Equipment>> getEquipment() {
        List<Equipment> equipment = equipmentRepository.findAll();
        return ResponseEntity.ok(equipment);
    }

    public ResponseEntity<Void> createEquipment(CreateEquipmentRequest request) {

        Equipment equipment = Equipment.builder()
                .name(request.getName())
                .capacity(request.getCapacity())
                .build();

        equipmentRepository.save(equipment);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteEquipment(Long id) {
        if (!equipmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        equipmentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public Equipment findById(Long id) {
        Optional<Equipment> equipmentOptional = equipmentRepository.findById(id);
        return equipmentOptional.orElse(null);
    }

    public ResponseEntity<Map<Equipment, Integer>> getPatientsTreatedByEquipment() {
        List<Equipment> equipment = equipmentRepository.findAll();
        List<Appointment> appointments = appointmentRepository.findAll();
        Map<Equipment, Integer> mapa = new HashMap<>();
        for (Equipment e : equipment) {
            int count = 0;
            for (Appointment a : appointments) {
                if (a.getDateTime().isBefore(LocalDateTime.now()) && a.getTherapy().getTherapyType().getRequiredEquipment().equals(e)) {
                    count++;
                }
            }
            mapa.put(e, count);
        }
        return ResponseEntity.ok(mapa);
    }
}
