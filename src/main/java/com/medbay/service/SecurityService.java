package com.medbay.service;

import com.medbay.domain.*;
import com.medbay.domain.enums.ActivityStatus;
import com.medbay.domain.request.CreatePatientRequest;
import com.medbay.domain.request.LoginRequest;
import com.medbay.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static com.medbay.domain.enums.Role.PATIENT;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PatientRepository patientRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    public ResponseEntity<Void> register(CreatePatientRequest request) {

        //TODO registration of deactivated users

        if(userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        else if(patientRepository.existsByMBO(request.getMBO())) {
            return ResponseEntity.status(CONFLICT).build();
        }

        Patient patient = Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .address(request.getAddress())
                .MBO(request.getMBO())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDateOfBirth())
                .status(ActivityStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .role(PATIENT)
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
        return ResponseEntity.ok(Map.of("accessToken", token));

    }


    public ResponseEntity<Void> sendTokenEmailForForgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
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
        passwordResetTokenRepository.delete(resetToken);
        return ResponseEntity.ok().build();
    }



}
