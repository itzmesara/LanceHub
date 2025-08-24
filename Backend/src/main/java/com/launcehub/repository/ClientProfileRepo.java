package com.launcehub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.launcehub.Model.ClientProfile;
import com.launcehub.Model.Users;

@Repository
public interface ClientProfileRepo extends JpaRepository<ClientProfile, Long> {
    Optional<ClientProfile> findByUser(Users user);
    Optional<ClientProfile> findByUserId(Long userId);
    boolean existsByUser(Users user);
    boolean existsByCompanyNameIgnoreCase(String companyName);
    
    @Query("SELECT cp FROM ClientProfile cp WHERE cp.user.email = :email")
    Optional<ClientProfile> findByUserEmail(@Param("email") String email);
}
