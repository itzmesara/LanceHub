package com.launcehub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.launcehub.Model.Application;
import com.launcehub.Model.ApplicationStatus;
import com.launcehub.Model.Projects;
import com.launcehub.Model.Users;
import com.launcehub.dto.ApplicationResponse;
import com.launcehub.exception.ResourceNotFoundException;
import com.launcehub.repository.ApplicationRepo;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepo applicationRepo;

    @Autowired
    private ProjectService projectService;

    public List<Application> getAllApplications() {
        return applicationRepo.findAll();
    }

    public Optional<Application> getApplicationById(Long id) {
        return applicationRepo.findById(id);
    }

    public Application saveApplication(Application application) {
        return applicationRepo.save(application);
    }

    public void deleteApplication(Long id) {
        applicationRepo.deleteById(id);
    }

    public List<Application> getApplicationsByProject(Projects project) {
        return applicationRepo.findByProjects(project);
    }

    public List<Application> getApplicationsByFreelancer(Users freelancer) {
        return applicationRepo.findByFreelancer(freelancer);
    }

    public boolean hasFreelancerApplied(Projects project, Users freelancer) {
        return applicationRepo.existsByProjectsAndFreelancer(project, freelancer);
    }

    public long getApplicationCountForProject(Projects project) {
        return applicationRepo.countApplicationsByProject(project);
    }

    // Client-specific methods
    public Page<Application> getApplicationsForClientProjects(String clientEmail, Pageable pageable) {
        return applicationRepo.findApplicationsForClientProjects(clientEmail, pageable);
    }

    public Application updateApplicationStatus(Long applicationId, ApplicationStatus status, String clientEmail) {
        Application application = applicationRepo.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", applicationId));
        
        // Verify that the client owns the project associated with this application
        if (!projectService.isProjectOwner(application.getProjects().getId(), clientEmail)) {
            throw new RuntimeException("Client does not own the project associated with this application");
        }
        
        application.setStatus(status);
        return applicationRepo.save(application);
    }

    public ApplicationResponse convertToResponse(Application application) {
        return new ApplicationResponse(
            application.getId(),
            application.getProjects(),
            application.getFreelancer(),
            application.getProposalMessage(),
            application.getStatus(),
            application.getCreatedAt()
        );
    }
}
