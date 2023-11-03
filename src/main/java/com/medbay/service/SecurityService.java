package com.medbay.service;

import com.medbay.domain.PatientDetails;
import com.medbay.domain.User;
import com.medbay.domain.request.CreateUserRequest;
import com.medbay.domain.request.LoginRequest;
import com.medbay.repository.PatientDetailsRepository;
import com.medbay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final PatientDetailsRepository patientDetailsRepository;

    public ResponseEntity<Void> register(CreateUserRequest request) {

        if(userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .active(true)
                .role(ROLE_PATIENT)
                .build();

        user = userRepository.save(user);

        PatientDetails patientDetails = PatientDetails.builder()
                .address(request.getAddress())
                .MBO(request.getMBO())
                .dateOfBirth(request.getDateOfBirth())
                .phoneNumber(request.getPhoneNumber())
                .user(user)
                .build();

        patientDetailsRepository.save(patientDetails);

        user.setPatientDetails(patientDetails);

        userRepository.save(user);

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
