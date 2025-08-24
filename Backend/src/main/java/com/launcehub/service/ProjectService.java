package com.launcehub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.launcehub.Model.Projects;
import com.launcehub.Model.Users;
import com.launcehub.Model.ProjectStatus;
import com.launcehub.repository.ProjectRepo;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepo projectRepo;

    public List<Projects> getAllProjects() {
        return projectRepo.findAll();
    }

    public Optional<Projects> getProjectById(Long id) {
        return projectRepo.findById(id);
    }

    public Projects saveProject(Projects project) {
        return projectRepo.save(project);
    }

    public void deleteProject(Long id) {
        projectRepo.deleteById(id);
    }

    public List<Projects> getProjectsByClient(Users client) {
        return projectRepo.findByClient(client);
    }

    public List<Projects> getProjectsByStatus(ProjectStatus status) {
        return projectRepo.findByStatus(status);
    }

    public List<Projects> getProjectsByFreelancer(Users freelancer) {
        return projectRepo.findByAssignedFreelancer(freelancer);
    }

    public List<Projects> getActiveProjects() {
        return projectRepo.findByStatusAndIsArchivedFalse(ProjectStatus.OPEN);
    }

    public boolean isProjectOwner(Long projectId, String clientEmail) {
        Optional<Projects> project = projectRepo.findById(projectId);
        return project.isPresent() && 
               project.get().getClient() != null && 
               project.get().getClient().getEmail().equalsIgnoreCase(clientEmail);
    }
}
