package com.launcehub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.launcehub.Model.Application;
import com.launcehub.Model.Projects;
import com.launcehub.Model.Users;

@Repository
public interface ApplicationRepo extends JpaRepository<Application, Long> {
    List<Application> findByProjects(Projects project);
    List<Application> findByFreelancer(Users freelancer);
    Optional<Application> findByProjectsAndFreelancer(Projects project, Users freelancer);
    List<Application> findByProjectsOrderByCreatedAtDesc(Projects project);
    boolean existsByProjectsAndFreelancer(Projects project, Users freelancer);
    
    @Query("SELECT a FROM Application a WHERE a.projects.client = :client")
    List<Application> findApplicationsByClient(@Param("client") Users client);
    
    @Query("SELECT COUNT(a) FROM Application a WHERE a.projects = :project")
    long countApplicationsByProject(@Param("project") Projects project);
    
    @Query("SELECT a FROM Application a WHERE a.projects.client.email = :clientEmail")
    Page<Application> findApplicationsForClientProjects(@Param("clientEmail") String clientEmail, Pageable pageable);
}
