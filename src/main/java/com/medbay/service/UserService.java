package com.medbay.service;

import com.medbay.domain.User;
import com.medbay.domain.enums.ActivityStatus;
import com.medbay.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;


    public ResponseEntity<Void> changeActivityStatus(String status, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(user.getStatus().equals(ActivityStatus.DEACTIVATED)) {
            userRepository.delete(user);
        }
        else{
            user.setStatus(ActivityStatus.valueOf(status.toUpperCase()));
            userRepository.save(user);
        }
        emailService.sendConfirmationEmail(user);
        return ResponseEntity.ok().build();
    }



//    public ResponseEntity<Void> deleteUser(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        user.setStatus(ActivityStatus.DEACTIVATED);
//        userRepository.save(user);
//        return ResponseEntity.ok().build();
//    }
}
