package com.medbay.service;

import com.medbay.domain.DTO.FacilityDTO;
import com.medbay.domain.Equipment;
import com.medbay.domain.TherapyType;
import com.medbay.domain.enums.Specialization;
import com.medbay.domain.request.CreateEquipmentRequest;
import com.medbay.repository.EquipmentRepository;
import com.medbay.repository.TherapyTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final TherapyTypeRepository therapyTypeRepository;

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
}
