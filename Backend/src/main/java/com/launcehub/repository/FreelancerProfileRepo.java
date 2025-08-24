package com.launcehub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.launcehub.Model.FreelancerProfile;
import com.launcehub.Model.Users;

@Repository
public interface FreelancerProfileRepo extends JpaRepository<FreelancerProfile, Long> {
    Optional<FreelancerProfile> findByUser(Users user);
    Optional<FreelancerProfile> findByUserId(Long userId);
    boolean existsByUser(Users user);
    
    @Query("SELECT fp FROM FreelancerProfile fp WHERE LOWER(fp.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(fp.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<FreelancerProfile> findByNameContaining(@Param("name") String name);
    
    @Query(value = "SELECT * FROM freelancer_profile WHERE skills IS NOT NULL AND skills LIKE CONCAT('%', :skill, '%')", nativeQuery = true)
    List<FreelancerProfile> findBySkill(@Param("skill") String skill);
}
