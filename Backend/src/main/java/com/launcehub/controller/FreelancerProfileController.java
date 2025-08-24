package com.launcehub.controller;

import com.launcehub.Model.FreelancerProfile;
import com.launcehub.service.FreelancerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/freelancers")
@CrossOrigin(origins = "http://localhost:3000")
public class FreelancerProfileController {

    @Autowired
    private FreelancerProfileService freelancerProfileService;

    // Get all freelancer profiles
    @GetMapping
    public ResponseEntity<List<FreelancerProfile>> getAllFreelancerProfiles() {
        List<FreelancerProfile> profiles = freelancerProfileService.getAllFreelancerProfiles();
        return ResponseEntity.ok(profiles);
    }

    // Get freelancer profile by ID
    @GetMapping("/{id}")
    public ResponseEntity<FreelancerProfile> getFreelancerProfileById(@PathVariable Long id) {
        Optional<FreelancerProfile> profile = freelancerProfileService.getFreelancerProfileById(id);
        return profile.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get freelancer profile by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<FreelancerProfile> getFreelancerProfileByUserId(@PathVariable Long userId) {
        Optional<FreelancerProfile> profile = freelancerProfileService.getFreelancerProfileByUserId(userId);
        return profile.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new freelancer profile
    @PostMapping
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfile> createFreelancerProfile(@RequestBody FreelancerProfile profile) {
        try {
            FreelancerProfile savedProfile = freelancerProfileService.saveFreelancerProfile(profile);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProfile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Update freelancer profile
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<FreelancerProfile> updateFreelancerProfile(
            @PathVariable Long id,
            @RequestBody FreelancerProfile profile) {
        try {
            Optional<FreelancerProfile> existingProfile = freelancerProfileService.getFreelancerProfileById(id);
            if (existingProfile.isPresent()) {
                FreelancerProfile updatedProfile = freelancerProfileService.saveFreelancerProfile(profile);
                return ResponseEntity.ok(updatedProfile);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Delete freelancer profile
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FREELANCER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFreelancerProfile(@PathVariable Long id) {
        try {
            if (freelancerProfileService.getFreelancerProfileById(id).isPresent()) {
                freelancerProfileService.deleteFreelancerProfile(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
