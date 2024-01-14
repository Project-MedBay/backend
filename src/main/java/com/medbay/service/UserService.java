package com.medbay.service;

import com.medbay.domain.Patient;
import com.medbay.domain.User;
import com.medbay.domain.enums.ActivityStatus;
import com.medbay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;


    public ResponseEntity<Void> changeActivityStatus(String status, Long id, String rejectionReason) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(status.equals(ActivityStatus.DEACTIVATED.name())) {
            emailService.sendRejectionEmail(user, rejectionReason);
            userRepository.delete(user);
        }
        else{
            user.setStatus(ActivityStatus.ACTIVE);
            userRepository.save(user);
        }
        emailService.sendConfirmationEmail(user);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteUser(Long id) {

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()));

        User user;

        if(id == null && isAdmin){
            return ResponseEntity.badRequest().build();
        } else if(id == null){
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } else {
            user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        user.setStatus(ActivityStatus.DEACTIVATED);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<Boolean> checkPassword(String password, Long id) {
        User user = getUserBasedOnIdAndRole(id);

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
        return ResponseEntity.ok(passwordMatches);
    }

    private User getUserBasedOnIdAndRole(Long id) {
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority()));

        if (id == null) {
            if (isAdmin) {
                return null;
            }
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

}
