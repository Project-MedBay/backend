package com.medbay.service;

import com.medbay.domain.Patient;
import com.medbay.domain.User;
import com.medbay.domain.request.CreatePatientRequest;
import com.medbay.domain.request.LoginRequest;
import com.medbay.repository.PatientRepository;
import com.medbay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

import static com.medbay.domain.enums.Role.ROLE_PATIENT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PatientRepository patientRepository;

    public ResponseEntity<Void> register(CreatePatientRequest request) {

        if(userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        Patient patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .MBO(request.getMBO())
                .appointments(new ArrayList<>())
                .dateOfBirth(request.getDateOfBirth())
                .active(true)
                .role(ROLE_PATIENT)
                .build();

        userRepository.save(patient);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<Map<String, String>> login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return ResponseEntity.ok(Map.of("accessToken", token, "refreshToken", refreshToken));

    }

    public ResponseEntity<String> refreshToken(Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String email = jwtService.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(jwtService.isTokenValid(refreshToken, user)) {
            String newToken = jwtService.generateAccessToken(user);
            return ResponseEntity.ok(newToken);
        } else {
            return new ResponseEntity<>("Invalid refresh token", UNAUTHORIZED);
        }
    }

}
