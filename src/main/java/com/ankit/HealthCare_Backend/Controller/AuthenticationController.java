package com.ankit.HealthCare_Backend.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.ankit.HealthCare_Backend.DTO.ForgotPasswordRequestDTO;
import com.ankit.HealthCare_Backend.DTO.LoginRequestDTO;
import com.ankit.HealthCare_Backend.DTO.LoginResponseDTO;
import com.ankit.HealthCare_Backend.DTO.MessageResponseDTO;
import com.ankit.HealthCare_Backend.DTO.RegisterRequestDTO;
import com.ankit.HealthCare_Backend.DTO.ResetPasswordRequestDTO;
import com.ankit.HealthCare_Backend.DTO.UserResponseDTO;
import com.ankit.HealthCare_Backend.Entity.Admin;
import com.ankit.HealthCare_Backend.Entity.Doctor;
import com.ankit.HealthCare_Backend.Entity.User;
import com.ankit.HealthCare_Backend.JWT.JwtService;
import com.ankit.HealthCare_Backend.Repository.AdminRepository;
import com.ankit.HealthCare_Backend.Repository.DoctorRepository;
import com.ankit.HealthCare_Backend.Repository.UserRepository;
import com.ankit.HealthCare_Backend.Service.CustomUserDetailsService;
import com.ankit.HealthCare_Backend.Service.AuthService.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private DoctorRepository doctorRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthService authService;

    // For normal user registration (Doctor/Patient)
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody RegisterRequestDTO registerRequest) {
        try {
            UserResponseDTO response = authService.registerUser(registerRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            // You might want to add proper error handling here
            throw e;
        }
    }

    // Unified login for Admin, Doctor, and Patient
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            // Check if it's an admin login
            Optional<Admin> adminOpt = adminRepo.findByEmailAndIsActive(loginRequest.getEmail(), true);
            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                if (passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
                    UserDetails adminDetails = org.springframework.security.core.userdetails.User
                            .withUsername(admin.getEmail())
                            .password(admin.getPassword())
                            .roles("ADMIN")
                            .build();
                    String jwt = jwtService.generateToken(adminDetails);
                    return ResponseEntity.ok(new LoginResponseDTO(jwt, "Admin login successful"));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponseDTO(null, "Invalid email or password"));
                }
            }
            
            // Normal user authentication (Doctor/Patient)
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    loginRequest.getPassword()
                )
            );
            
            // If we get here, user is authenticated
            final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());

            // Check if it's a doctor
            boolean isDoctor = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_DOCTOR"));

            if (isDoctor) {
                User user = userRepo.findByEmail(userDetails.getUsername());
                Doctor doctor = doctorRepo.findByUserId(user.getId());

                if (doctor != null && doctor.isApproved()) {
                    final String jwt = jwtService.generateToken(userDetails);
                    return ResponseEntity.ok(new LoginResponseDTO(jwt, "Doctor login successful"));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new LoginResponseDTO(null, "Your doctor account is not approved yet. Contact admin or wait for approval.")
                    );
                }
            } else {
                final String jwt = jwtService.generateToken(userDetails);
                return ResponseEntity.ok(new LoginResponseDTO(jwt, "Patient login successful"));
            }

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new LoginResponseDTO(null, "Invalid email or password")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new LoginResponseDTO(null, "An error occurred during login: " + e.getMessage())
            );
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponseDTO> forgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        try {
            String message = authService.forgotPassword(request.getEmail());
            return ResponseEntity.ok(new MessageResponseDTO(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponseDTO> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        try {
            String message = authService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(new MessageResponseDTO(message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO(e.getMessage()));
        }
    }
}