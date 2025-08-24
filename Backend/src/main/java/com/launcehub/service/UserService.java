package com.launcehub.service;

import com.launcehub.Model.Users;
import com.launcehub.dto.AuthRequest;
import com.launcehub.dto.AuthResponse;
import com.launcehub.dto.RegisterRequest;
import com.launcehub.repository.UserRepo;
import com.launcehub.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        if (userRepo.existsByEmailIgnoreCase(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already taken!");
        }

        Users user = new Users();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(registerRequest.getRole());
        user.setActive(true);
        user.setCreated_at(LocalDateTime.now());
        user.setUpdated_at(LocalDateTime.now());

        userRepo.save(user);

        String jwt = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(
                jwt,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        ));
    }

    public ResponseEntity<?> authenticateUser(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authRequest.getEmail());

        Users user = userRepo.findByEmailIgnoreCase(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(new AuthResponse(
                jwt,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name()
        ));
    }

    public Users getCurrentUser(String email) {
        return userRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public boolean existsByEmail(String email) {
        return userRepo.existsByEmailIgnoreCase(email);
    }
}
