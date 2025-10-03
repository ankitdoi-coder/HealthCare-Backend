package com.ankit.HealthCare_Backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ankit.HealthCare_Backend.Entity.User;
import com.ankit.HealthCare_Backend.Service.AuthService.AuthService;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    @Autowired
    AuthService authService;

    // to Register New User(localhost:8080/api/auth/register)
    @PostMapping("/auth/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        System.out.println("Request received: " + user.getEmail()); // Debug log
        User savedUser = authService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // to login the user
    @PostMapping("/auth/login")
    public void login() {

    }
}
