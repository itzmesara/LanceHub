package com.launcehub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.launcehub.Model.ClientProfile;
import com.launcehub.Model.Users;
import com.launcehub.exception.ResourceNotFoundException;
import com.launcehub.repository.ClientProfileRepo;
import com.launcehub.repository.UserRepo;

@Service
public class ClientProfileService {

    @Autowired
    private ClientProfileRepo clientProfileRepo;

    @Autowired
    private UserRepo userRepo;

    public List<ClientProfile> getAllClientProfiles() {
        return clientProfileRepo.findAll();
    }

    public Optional<ClientProfile> getClientProfileById(Long id) {
        return clientProfileRepo.findById(id);
    }

    public ClientProfile saveClientProfile(ClientProfile clientProfile) {
        return clientProfileRepo.save(clientProfile);
    }

    public void deleteClientProfile(Long id) {
        clientProfileRepo.deleteById(id);
    }

    public Optional<ClientProfile> getClientProfileByUser(Users user) {
        return clientProfileRepo.findByUser(user);
    }

    public Optional<ClientProfile> getClientProfileByUserId(Long userId) {
        return clientProfileRepo.findByUserId(userId);
    }

    public boolean existsByUser(Users user) {
        return clientProfileRepo.existsByUser(user);
    }

    public Optional<ClientProfile> getClientProfileByUserEmail(String email) {
        return clientProfileRepo.findByUserEmail(email);
    }

    public ClientProfile createClientProfileForUser(ClientProfile clientProfile, String email) {
        Users user = userRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        // Ensure the profile is associated with the correct user
        clientProfile.setUser(user);
        return clientProfileRepo.save(clientProfile);
    }

    public ClientProfile updateClientProfile(Long id, ClientProfile clientProfileDetails) {
        ClientProfile existingProfile = clientProfileRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClientProfile", "id", id));
        
        // Update fields based on the actual ClientProfile model structure
        if (clientProfileDetails.getCompanyName() != null) {
            existingProfile.setCompanyName(clientProfileDetails.getCompanyName());
        }
        if (clientProfileDetails.getContactName() != null) {
            existingProfile.setContactName(clientProfileDetails.getContactName());
        }
        if (clientProfileDetails.getBio() != null) {
            existingProfile.setBio(clientProfileDetails.getBio());
        }
        if (clientProfileDetails.getProfilePictureUri() != null) {
            existingProfile.setProfilePictureUri(clientProfileDetails.getProfilePictureUri());
        }
        
        return clientProfileRepo.save(existingProfile);
    }

    public boolean isProfileOwner(Long profileId, String email) {
        Optional<ClientProfile> profile = clientProfileRepo.findById(profileId);
        return profile.isPresent() && profile.get().getUser().getEmail().equals(email);
    }

    public ClientProfile updateClientProfileByEmail(String email, ClientProfile clientProfileDetails) {
        ClientProfile existingProfile = clientProfileRepo.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("ClientProfile", "email", email));
        
        // Update fields based on the actual ClientProfile model structure
        if (clientProfileDetails.getCompanyName() != null) {
            existingProfile.setCompanyName(clientProfileDetails.getCompanyName());
        }
        if (clientProfileDetails.getContactName() != null) {
            existingProfile.setContactName(clientProfileDetails.getContactName());
        }
        if (clientProfileDetails.getBio() != null) {
            existingProfile.setBio(clientProfileDetails.getBio());
        }
        if (clientProfileDetails.getProfilePictureUri() != null) {
            existingProfile.setProfilePictureUri(clientProfileDetails.getProfilePictureUri());
        }
        
        return clientProfileRepo.save(existingProfile);
    }
}
