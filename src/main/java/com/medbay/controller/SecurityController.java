package com.medbay.controller;

import com.medbay.domain.User;
import com.medbay.domain.request.CreatePatientRequest;
import com.medbay.domain.request.CreateTherapyRequest;
import com.medbay.domain.request.LoginRequest;
import com.medbay.repository.UserRepository;
import com.medbay.service.EmailService;
import com.medbay.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SecurityController {

    private final SecurityService securityService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody CreatePatientRequest request) {
        return securityService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        return securityService.login(request);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody Map<String, String> request) {
        return securityService.refreshToken(request);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> changePasswordEmail(@RequestParam String email) {
        return securityService.sendTokenEmailForForgotPassword(email);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestParam String token,
                                               @RequestParam String password) {
        return securityService.changePassword(token, password);
    }

    @PostMapping("/create-new-therapy")
    public ResponseEntity<Void> createNewTherapy(@RequestBody CreateTherapyRequest request) {
        return securityService.createNewTherapy(request);
    }

}
