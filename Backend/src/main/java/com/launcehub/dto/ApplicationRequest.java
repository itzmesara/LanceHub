package com.launcehub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ApplicationRequest {
    
    @NotNull(message = "Project ID is required")
    @Positive(message = "Project ID must be positive")
    private Long projectId;

    @NotBlank(message = "Cover letter is required")
    private String coverLetter;

    @NotNull(message = "Proposed budget is required")
    @Positive(message = "Proposed budget must be positive")
    private Double proposedBudget;

    @NotBlank(message = "Estimated timeline is required")
    private String estimatedTimeline;

    @NotBlank(message = "Relevant experience is required")
    private String relevantExperience;

    // Constructors
    public ApplicationRequest() {}

    public ApplicationRequest(Long projectId, String coverLetter, Double proposedBudget, 
                             String estimatedTimeline, String relevantExperience) {
        this.projectId = projectId;
        this.coverLetter = coverLetter;
        this.proposedBudget = proposedBudget;
        this.estimatedTimeline = estimatedTimeline;
        this.relevantExperience = relevantExperience;
    }

    // Getters and Setters
    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public Double getProposedBudget() {
        return proposedBudget;
    }

    public void setProposedBudget(Double proposedBudget) {
        this.proposedBudget = proposedBudget;
    }

    public String getEstimatedTimeline() {
        return estimatedTimeline;
    }

    public void setEstimatedTimeline(String estimatedTimeline) {
        this.estimatedTimeline = estimatedTimeline;
    }

    public String getRelevantExperience() {
        return relevantExperience;
    }

    public void setRelevantExperience(String relevantExperience) {
        this.relevantExperience = relevantExperience;
    }
}
