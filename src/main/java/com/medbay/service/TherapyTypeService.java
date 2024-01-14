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
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TherapyTypeService {
    private final TherapyTypeRepository therapyTypeRepository;
    private final EquipmentRepository equipmentRepository;

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final int CODE_LENGTH = 5;
    private final Random random = new Random();

    public ResponseEntity<List<TherapyType>> getTherapyType() {
        List<TherapyType> therapyTypes = therapyTypeRepository.findAll();
        return ResponseEntity.ok(therapyTypes);
    }


    public String generateTherapyCode() {
        StringBuilder code = new StringBuilder("#");

        for (int i = 0; i < CODE_LENGTH; i++) {
            if (i % 2 == 0) {
                int index = random.nextInt(NUMBERS.length());
                code.append(NUMBERS.charAt(index));
            } else {
                int index = random.nextInt(LETTERS.length());
                code.append(LETTERS.charAt(index));
            }
        }
        return code.toString();
    }


    public ResponseEntity<Map<String, Object>> createTherapyType(CreateTherapyTypeRequest request) {
        Optional<Equipment> equipment = equipmentRepository.findById(request.getRequiredEquipmentId());
        if (equipment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String therapyCode = generateTherapyCode();
        while(therapyTypeRepository.existsByTherapyCode(therapyCode)){
            therapyCode = generateTherapyCode();
        }


        TherapyType therapyType = TherapyType.builder()
                .description(request.getDescription())
                .name(request.getName())
                .therapyCode(therapyCode)
                .bodyPart(request.getBodyPart())
                .numOfSessions(request.getNumberOfSessions())
                .requiredEquipment(equipment.get())
                .build();

        TherapyType savedTherapyType = therapyTypeRepository.save(therapyType);

        return ResponseEntity.ok(Map.of(
                "id", savedTherapyType.getId(),
                "therapyTypeCode", savedTherapyType.getTherapyCode()
        ));
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
