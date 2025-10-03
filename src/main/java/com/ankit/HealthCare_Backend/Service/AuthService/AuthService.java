package com.ankit.HealthCare_Backend.Service.AuthService;

import org.springframework.stereotype.Service;

import com.ankit.HealthCare_Backend.Entity.User;

@Service
public interface AuthService {
    User saveUser(User user);
}
