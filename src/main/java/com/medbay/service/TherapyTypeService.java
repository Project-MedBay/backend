package com.medbay.service;

import com.medbay.domain.Equipment;
import com.medbay.domain.TherapyType;
import com.medbay.domain.request.CreateTherapyTypeRequest;
import com.medbay.repository.EquipmentRepository;
import com.medbay.repository.TherapyTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TherapyTypeService {
    private final TherapyTypeRepository therapyTypeRepository;
    private final EquipmentRepository equipmentRepository;

    public ResponseEntity<List<TherapyType>> getTherapyType() {
        List<TherapyType> therapyTypes = therapyTypeRepository.findAll();
        return ResponseEntity.ok(therapyTypes);
    }

    public ResponseEntity<Void> createTherapyType(CreateTherapyTypeRequest request) {
        Optional<Equipment> equipment = equipmentRepository.findById(request.getRequiredEquipmentId());
        if (equipment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        TherapyType therapyType = TherapyType.builder()
                .description(request.getDescription())
                .requiredEquipment(equipment.get())
                .build();

        therapyTypeRepository.save(therapyType);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteTherapyType(Long id) {
        if (!therapyTypeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        therapyTypeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public TherapyType findById(Long id) {
        Optional<TherapyType> therapyType = therapyTypeRepository.findById(id);
        return therapyType.orElseThrow(() -> new RuntimeException("Therapy Type not found with id: " + id));
    }
}
