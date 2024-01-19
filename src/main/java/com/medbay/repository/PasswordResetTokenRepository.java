package com.medbay.repository;

import com.medbay.domain.PasswordResetToken;
import com.medbay.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);
    
    boolean existsByUser(User user);

    Optional<PasswordResetToken> findByUser(User user);
}
