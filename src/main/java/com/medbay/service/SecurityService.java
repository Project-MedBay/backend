package com.medbay.service;

import com.medbay.domain.*;
import com.medbay.domain.enums.ActivityStatus;
import com.medbay.domain.enums.TherapyStatus;
import com.medbay.domain.request.CreatePatientRequest;
import com.medbay.domain.request.CreateTherapyRequest;
import com.medbay.domain.request.LoginRequest;
import com.medbay.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.medbay.domain.enums.Role.ROLE_PATIENT;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PatientRepository patientRepository;
    private final TherapyTypeRepository therapyTypeRepository;
    private final TherapyRepository therapyRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    public ResponseEntity<Void> register(CreatePatientRequest request) {

        if(userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        else if(patientRepository.existsByMBOOrOIB(request.getMBO(), request.getOIB())) {
            return ResponseEntity.status(CONFLICT).build();
        }

        Patient patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .MBO(request.getMBO())
                .OIB(request.getOIB())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDateOfBirth())
                .status(ActivityStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .role(ROLE_PATIENT)
                .build();

        patientRepository.save(patient);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<Map<String, String>> login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


//        if(user.getStatus().equals(ActivityStatus.PENDING)) {
//            return ResponseEntity.status(UNAUTHORIZED).build();
//        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


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

    public ResponseEntity<Void> sendTokenEmailForForgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .build();
        passwordResetTokenRepository.save(resetToken);
        emailService.sendChangePasswordEmail(email, token);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> changePassword(String token, String password) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }



}
