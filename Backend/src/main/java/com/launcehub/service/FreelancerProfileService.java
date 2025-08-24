package com.launcehub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.launcehub.Model.FreelancerProfile;
import com.launcehub.Model.Users;
import com.launcehub.repository.FreelancerProfileRepo;

@Service
public class FreelancerProfileService {

    @Autowired
    private FreelancerProfileRepo freelancerProfileRepo;

    public List<FreelancerProfile> getAllFreelancerProfiles() {
        return freelancerProfileRepo.findAll();
    }

    public Optional<FreelancerProfile> getFreelancerProfileById(Long id) {
        return freelancerProfileRepo.findById(id);
    }

    public FreelancerProfile saveFreelancerProfile(FreelancerProfile freelancerProfile) {
        return freelancerProfileRepo.save(freelancerProfile);
    }

    public void deleteFreelancerProfile(Long id) {
        freelancerProfileRepo.deleteById(id);
    }

    public Optional<FreelancerProfile> getFreelancerProfileByUser(Users user) {
        return freelancerProfileRepo.findByUser(user);
    }

    public Optional<FreelancerProfile> getFreelancerProfileByUserId(Long userId) {
        return freelancerProfileRepo.findByUserId(userId);
    }

    public boolean existsByUser(Users user) {
        return freelancerProfileRepo.existsByUser(user);
    }

    public List<FreelancerProfile> searchByName(String name) {
        return freelancerProfileRepo.findByNameContaining(name);
    }

    public List<FreelancerProfile> findBySkill(String skill) {
        return freelancerProfileRepo.findBySkill(skill);
    }
}
