package com.launcehub.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.prepost.PreAuthorize;

import com.launcehub.Model.Projects;
import com.launcehub.Model.ProjectStatus;
import com.launcehub.Model.Users;
import com.launcehub.service.ProjectService;
import com.launcehub.service.UserService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Projects> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Projects> getProjectById(@PathVariable Long id) {
        Optional<Projects> project = projectService.getProjectById(id);
        return project.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Projects> createProject(@RequestBody Projects project,
                                                  @AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        Users client = userService.getCurrentUser(principal.getUsername());
        project.setClient(client);
        if (project.getStatus() == null) {
            project.setStatus(ProjectStatus.OPEN);
        }
        if (project.getCreatedAt() == null) {
            project.setCreatedAt(LocalDateTime.now());
        }
        Projects saved = projectService.saveProject(project);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Projects> updateProject(@PathVariable Long id, @RequestBody Projects projectDetails) {
        Optional<Projects> project = projectService.getProjectById(id);
        if (project.isPresent()) {
            projectDetails.setId(id);
            return ResponseEntity.ok(projectService.saveProject(projectDetails));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
