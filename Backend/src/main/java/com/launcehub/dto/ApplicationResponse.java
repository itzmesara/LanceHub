package com.launcehub.dto;

import java.time.LocalDateTime;

import com.launcehub.Model.ApplicationStatus;
import com.launcehub.Model.Projects;
import com.launcehub.Model.Users;

public class ApplicationResponse {
    private Long id;
    private Projects project;
    private Users freelancer;
    private String proposalMessage;
    private ApplicationStatus status;
    private LocalDateTime createdAt;

    // Constructors
    public ApplicationResponse() {}

    public ApplicationResponse(Long id, Projects project, Users freelancer, 
                             String proposalMessage, ApplicationStatus status, 
                             LocalDateTime createdAt) {
        this.id = id;
        this.project = project;
        this.freelancer = freelancer;
        this.proposalMessage = proposalMessage;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Projects getProject() {
        return project;
    }

    public void setProject(Projects project) {
        this.project = project;
    }

    public Users getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(Users freelancer) {
        this.freelancer = freelancer;
    }

    public String getProposalMessage() {
        return proposalMessage;
    }

    public void setProposalMessage(String proposalMessage) {
        this.proposalMessage = proposalMessage;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
