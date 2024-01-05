package com.medbay.controller;

import com.medbay.domain.Equipment;
import com.medbay.domain.request.CreateEquipmentRequest;
import com.medbay.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<List<Equipment>> getEquipment(){
        return equipmentService.getEquipment();
    }
    @PostMapping
    public ResponseEntity<Void> createEquipment(@RequestBody CreateEquipmentRequest equipment){
        return equipmentService.createEquipment(equipment);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable("id") Long id) {
        return equipmentService.deleteEquipment(id);
    }
    @GetMapping("/manage/facility")
    public ResponseEntity<List<Equipment>> getResources() {
        return equipmentService.getEquipment();
    }
}
