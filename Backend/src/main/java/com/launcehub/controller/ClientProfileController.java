package com.launcehub.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.launcehub.Model.ClientProfile;
import com.launcehub.exception.ResourceNotFoundException;
import com.launcehub.service.ClientProfileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/client-profiles")
public class ClientProfileController {

    @Autowired
    private ClientProfileService clientProfileService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ClientProfile> getAllClientProfiles() {
        return clientProfileService.getAllClientProfiles();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clientProfileService.isProfileOwner(#id, authentication.principal.username)")
    public ResponseEntity<ClientProfile> getClientProfileById(@PathVariable Long id) {
        ClientProfile clientProfile = clientProfileService.getClientProfileById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClientProfile", "id", id));
        return ResponseEntity.ok(clientProfile);
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientProfile> createClientProfile(@RequestBody ClientProfile clientProfile, 
                                                           @AuthenticationPrincipal UserDetails principal) {
        // Ensure the profile is created for the authenticated user
        ClientProfile savedProfile = clientProfileService.createClientProfileForUser(clientProfile, principal.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfile);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT') and @clientProfileService.isProfileOwner(#id, authentication.principal.username)")
    public ResponseEntity<ClientProfile> updateClientProfile(@PathVariable Long id, @RequestBody ClientProfile clientProfileDetails) {
        ClientProfile updatedProfile = clientProfileService.updateClientProfile(id, clientProfileDetails);
        return ResponseEntity.ok(updatedProfile);
    }

    @PutMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientProfile> updateMyClientProfile(@RequestBody ClientProfile clientProfileDetails, 
                                                             @AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        ClientProfile updatedProfile = clientProfileService.updateClientProfileByEmail(principal.getUsername(), clientProfileDetails);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clientProfileService.isProfileOwner(#id, authentication.principal.username)")
    public ResponseEntity<Void> deleteClientProfile(@PathVariable Long id) {
        clientProfileService.deleteClientProfile(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-profile")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientProfile> getMyProfile(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        ClientProfile clientProfile = clientProfileService.getClientProfileByUserEmail(principal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("ClientProfile", "email", principal.getUsername()));
        
        return ResponseEntity.ok(clientProfile);
    }
}
