package com.launcehub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.launcehub.Model.Users;

@Repository
public interface UserRepo extends JpaRepository<Users,Long>{
    Optional<Users> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}
