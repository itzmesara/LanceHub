package com.launcehub.service;

import org.springframework.stereotype.Service;

import com.launcehub.model.User;
import com.launcehub.repository.UserRepo;

@Service
public class UserService {
    UserRepo userRepo;

    public User getUser(User user)
    {
        return userRepo.save(user);
    }

}
