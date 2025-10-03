package com.ankit.HealthCare_Backend.Service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ankit.HealthCare_Backend.Entity.User;
import com.ankit.HealthCare_Backend.Repository.UserRepository;
@Service
public class AuthServicesImpl implements AuthService{
    @Autowired
    UserRepository userRepo;

    @Override
    public User saveUser(User user) {
        User savedUser=userRepo.save(user);
        return savedUser;
    }
    
}
