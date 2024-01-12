package com.medbay.controller;

import com.medbay.domain.DTO.FacilityDTO;
import com.medbay.domain.request.CreateEquipmentRequest;
import com.medbay.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipment")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EquipmentController {

    private final EquipmentService equipmentService;


    @PostMapping
    public ResponseEntity<Void> createEquipment(@RequestBody CreateEquipmentRequest equipment){
        return equipmentService.createEquipment(equipment);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable("id") Long id) {
        return equipmentService.deleteEquipment(id);
    }
    @GetMapping("/facility")
    public ResponseEntity<FacilityDTO> getResources() {
        return equipmentService.getFacilities();
    }
}
