package com.medbay.service;

import com.medbay.domain.DTO.FacilityDTO;
import com.medbay.domain.Equipment;
import com.medbay.domain.TherapyType;
import com.medbay.domain.request.CreateEquipmentRequest;
import com.medbay.repository.EquipmentRepository;
import com.medbay.repository.TherapyTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final TherapyTypeRepository therapyTypeRepository;

    public ResponseEntity<FacilityDTO> getFacilities() {
        List<Equipment> equipment = equipmentRepository.findAll();
        List<TherapyType> therapyTypes = therapyTypeRepository.findAll();
        FacilityDTO facilityDTO = new FacilityDTO(equipment, therapyTypes);
        return ResponseEntity.ok(facilityDTO);
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
}
