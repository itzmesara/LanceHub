package com.launcehub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.launcehub.Model.Projects;
import com.launcehub.Model.Users;
import com.launcehub.Model.ProjectStatus;

@Repository
public interface ProjectRepo extends JpaRepository<Projects, Long> {
    List<Projects> findByClient(Users client);
    List<Projects> findByStatus(ProjectStatus status);
    List<Projects> findByAssignedFreelancer(Users freelancer);
    List<Projects> findByClientAndStatus(Users client, ProjectStatus status);
    List<Projects> findByTitleContainingIgnoreCase(String title);
    List<Projects> findByClientAndIsArchivedFalse(Users client);
    List<Projects> findByStatusAndIsArchivedFalse(ProjectStatus status);
    
    @Query("SELECT p FROM Projects p WHERE p.client = :client AND p.isArchived = false")
    List<Projects> findActiveProjectsByClient(@Param("client") Users client);
    
    @Query("SELECT p FROM Projects p WHERE p.assignedFreelancer = :freelancer AND p.isArchived = false")
    List<Projects> findActiveProjectsByFreelancer(@Param("freelancer") Users freelancer);
}
