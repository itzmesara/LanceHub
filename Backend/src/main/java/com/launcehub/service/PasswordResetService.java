package com.launcehub.service;

import com.launcehub.Model.Users;
import com.launcehub.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final Map<String, ResetToken> resetTokens = new HashMap<>();

    public void sendPasswordResetEmail(String email) {
        Users user = userRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();
        resetTokens.put(token, new ResetToken(user.getId(), LocalDateTime.now().plusHours(1)));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, use this token: " + token);
        
        mailSender.send(message);
    }

    public void resetPassword(String token, String newPassword) {
        ResetToken resetToken = resetTokens.get(token);
        if (resetToken == null || resetToken.getExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired token");
        }

        Users user = userRepo.findById(resetToken.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        
        resetTokens.remove(token);
    }

    private static class ResetToken {
        private final Long userId;
        private final LocalDateTime expiry;

        public ResetToken(Long userId, LocalDateTime expiry) {
            this.userId = userId;
            this.expiry = expiry;
        }

        public Long getUserId() {
            return userId;
        }

        public LocalDateTime getExpiry() {
            return expiry;
        }
    }
}
