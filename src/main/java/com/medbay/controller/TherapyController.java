package com.medbay.controller;

import com.medbay.domain.Therapy;
import com.medbay.domain.request.CreateTherapyRequest;
import com.medbay.service.TherapyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/therapy")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TherapyController {
    private final TherapyService therapyService;

    @GetMapping
    public ResponseEntity<List<Therapy>> getTherapies() {
        return therapyService.getTherapies();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTherapy(@PathVariable("id") Long id) {
        return therapyService.deleteTherapy(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createNewTherapy(@RequestBody CreateTherapyRequest request) {
        return therapyService.createNewTherapy(request);
    }

    @GetMapping("/verifications")
    public ResponseEntity<List<Therapy>> getTherapyRequests() {
        return therapyService.getTherapyRequests();
    }

}
