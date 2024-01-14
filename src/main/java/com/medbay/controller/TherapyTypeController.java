package com.medbay.controller;

import com.medbay.domain.TherapyType;
import com.medbay.domain.request.CreateTherapyTypeRequest;
import com.medbay.service.TherapyTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/therapyType")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TherapyTypeController {
    private final TherapyTypeService therapyTypeService;

    @GetMapping
    public ResponseEntity<List<TherapyType>> getTherapyType() {
        return therapyTypeService.getTherapyType();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTherapyType(@RequestBody CreateTherapyTypeRequest therapyType,
                                                  @PathVariable("id") Long id) {
        return therapyTypeService.updateTherapyType(therapyType, id);
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createTherapyType(@RequestBody CreateTherapyTypeRequest therapyType) {
        return therapyTypeService.createTherapyType(therapyType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTherapyType(@PathVariable("id") Long id) {
        return therapyTypeService.deleteTherapyType(id);
    }

}
