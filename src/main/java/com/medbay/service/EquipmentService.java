package com.medbay.service;

import com.medbay.domain.Equipment;
import com.medbay.domain.request.CreateEquipmentRequest;
import com.medbay.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentService {
    private final EquipmentRepository equipmentRepository;
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
}
