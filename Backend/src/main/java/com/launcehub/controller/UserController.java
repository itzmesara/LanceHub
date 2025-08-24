package com.launcehub.controller;

import com.launcehub.Model.Users;
import com.launcehub.dto.AuthResponse;
import com.launcehub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        Users user = userService.getCurrentUser(principal.getUsername());
        AuthResponse response = new AuthResponse(null, user.getId(), user.getName(), user.getEmail(), user.getRole().name());
        return ResponseEntity.ok(response);
    }
}
