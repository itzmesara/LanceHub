package com.launcehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.launcehub.model.User;

@Repository
public interface UserRepo extends JpaRepository<User,Long>{
    
}
