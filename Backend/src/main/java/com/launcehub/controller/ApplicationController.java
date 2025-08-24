package com.launcehub.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.launcehub.Model.Application;
import com.launcehub.Model.ApplicationStatus;
import com.launcehub.Model.Projects;
import com.launcehub.Model.Users;
import com.launcehub.dto.ApplicationRequest;
import com.launcehub.dto.ApplicationResponse;
import com.launcehub.exception.ResourceNotFoundException;
import com.launcehub.service.ApplicationService;
import com.launcehub.service.ProjectService;
import com.launcehub.service.UserService;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<Application> getAllApplications() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        Optional<Application> application = applicationService.getApplicationById(id);
        return application.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('FREELANCER')")
    public Application createApplication(@RequestBody ApplicationRequest applicationRequest) {
        // Fetch the project using the projectId from the request
        Projects project = projectService.getProjectById(applicationRequest.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", applicationRequest.getProjectId()));

        // Get the current authenticated user's email from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Get the current authenticated user (freelancer)
        Users freelancer = userService.getCurrentUser(email);

        // Create a new Application entity
        Application application = new Application();
        application.setProjects(project);
        application.setFreelancer(freelancer);
        application.setProposalMessage(applicationRequest.getCoverLetter());
        application.setCreatedAt(java.time.LocalDateTime.now());
        
        return applicationService.saveApplication(application);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<Application> updateApplication(@PathVariable Long id, @RequestBody Application applicationDetails) {
        Optional<Application> application = applicationService.getApplicationById(id);
        if (application.isPresent()) {
            applicationDetails.setId(id);
            return ResponseEntity.ok(applicationService.saveApplication(applicationDetails));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    // Client-specific endpoints
    @GetMapping("/client")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Page<ApplicationResponse>> getClientApplications(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        String clientEmail = authentication.getName();
        Pageable pageable = PageRequest.of(page, size, 
            Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        
        Page<Application> applications = applicationService.getApplicationsForClientProjects(clientEmail, pageable);
        Page<ApplicationResponse> response = applications.map(applicationService::convertToResponse);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam ApplicationStatus status,
            Authentication authentication) {
        
        String clientEmail = authentication.getName();
        Application updatedApplication = applicationService.updateApplicationStatus(id, status, clientEmail);
        ApplicationResponse response = applicationService.convertToResponse(updatedApplication);
        
        return ResponseEntity.ok(response);
    }
}
