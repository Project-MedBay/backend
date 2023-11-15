package com.medbay.controller;

import com.medbay.domain.Session;
import com.medbay.domain.request.CreateSessionRequest;
import com.medbay.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SessionController {
    private final SessionService sessionService;

    @GetMapping
    public ResponseEntity<List<Session>> getSessions(){
        return sessionService.getSessions();
    }
    @PostMapping
    public ResponseEntity<Void> createSession(@RequestBody CreateSessionRequest session){
        return sessionService.createSession(session);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable("id") Long id){
        return sessionService.deleteSession(id);
    }
}
