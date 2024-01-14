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


    public ResponseEntity<Long> createTherapyType(CreateTherapyTypeRequest request) {
        Optional<Equipment> equipment = equipmentRepository.findById(request.getRequiredEquipmentId());
        if (equipment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        TherapyType therapyType = TherapyType.builder()
                .description(request.getDescription())
                .therapyCode(request.getTherapyCode())
                .name(request.getName())
                .bodyPart(request.getBodyPart())
                .numOfSessions(request.getNumberOfSessions())
                .requiredEquipment(equipment.get())
                .build();

        TherapyType savedTherapyType = therapyTypeRepository.save(therapyType);
        return ResponseEntity.ok(savedTherapyType.getId());
    }

    public ResponseEntity<Void> deleteTherapyType(Long id) {
        if (!therapyTypeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        therapyTypeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> updateTherapyType(CreateTherapyTypeRequest therapyType, Long id) {
        Optional<TherapyType> therapyTypeOptional = therapyTypeRepository.findById(id);
        if (therapyTypeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Equipment> equipment = equipmentRepository.findById(therapyType.getRequiredEquipmentId());
        if (equipment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        TherapyType therapyTypeToUpdate = therapyTypeOptional.get();
        therapyTypeToUpdate.setBodyPart(therapyType.getBodyPart());
        therapyTypeToUpdate.setDescription(therapyType.getDescription());
        therapyTypeToUpdate.setName(therapyType.getName());
        therapyTypeToUpdate.setNumOfSessions(therapyType.getNumberOfSessions());
        therapyTypeToUpdate.setRequiredEquipment(equipment.get());

        therapyTypeRepository.save(therapyTypeToUpdate);
        return ResponseEntity.ok().build();
    }
}
