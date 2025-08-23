package com.launcehub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.launcehub.Model.Users;
import com.launcehub.repository.UserRepo;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    public void createUser(Users users) {
        userRepo.save(users);
    }
}
