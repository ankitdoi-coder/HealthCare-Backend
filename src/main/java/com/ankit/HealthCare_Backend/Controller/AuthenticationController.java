package com.ankit.HealthCare_Backend.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.ankit.HealthCare_Backend.DTO.LoginRequestDTO;
import com.ankit.HealthCare_Backend.DTO.LoginResponseDTO;
import com.ankit.HealthCare_Backend.DTO.RegisterRequestDTO;
import com.ankit.HealthCare_Backend.DTO.UserResponseDTO;
import com.ankit.HealthCare_Backend.JWT.JwtService;
import com.ankit.HealthCare_Backend.Service.CustomUserDetailsService;
import com.ankit.HealthCare_Backend.Service.AuthService.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    // injection
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;
    // For normal user registration (Doctor/Patient)
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequestDTO registerRequest) {
        return new ResponseEntity<>(authService.registerUser(registerRequest), HttpStatus.CREATED);
    }

    // For normal user login (Doctor/Patient)
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        // If we get here, user is authenticated
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new LoginResponseDTO(jwt));
    }

   
}
