package com.medbay.service;

import com.medbay.domain.FacilityData;
import com.medbay.repository.EquipmentRepository;
import com.medbay.repository.TherapyTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacilityDataService {
    private final EquipmentRepository equipmentRepository;
    private final TherapyTypeRepository therapyTypeRepository;
    public ResponseEntity<FacilityData> getFacilityData() {
        FacilityData facilityData = new FacilityData();
        facilityData.setEquipmentList(equipmentRepository.findAll());
        facilityData.setTherapyTypeList(therapyTypeRepository.findAll());

        return ResponseEntity.ok(facilityData);
    }

}
