package com.ankit.HealthCare_Backend.Controller;

<<<<<<< HEAD
import java.util.Optional;

=======
>>>>>>> origin/main
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
<<<<<<< HEAD
import org.springframework.security.crypto.password.PasswordEncoder;
=======
>>>>>>> origin/main
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

<<<<<<< HEAD
=======
import lombok.RequiredArgsConstructor;

>>>>>>> origin/main
import com.ankit.HealthCare_Backend.DTO.ForgotPasswordRequestDTO;
import com.ankit.HealthCare_Backend.DTO.LoginRequestDTO;
import com.ankit.HealthCare_Backend.DTO.LoginResponseDTO;
import com.ankit.HealthCare_Backend.DTO.MessageResponseDTO;
import com.ankit.HealthCare_Backend.DTO.RegisterRequestDTO;
import com.ankit.HealthCare_Backend.DTO.ResetPasswordRequestDTO;
import com.ankit.HealthCare_Backend.DTO.UserResponseDTO;
<<<<<<< HEAD
import com.ankit.HealthCare_Backend.Entity.Admin;
import com.ankit.HealthCare_Backend.Entity.Doctor;
import com.ankit.HealthCare_Backend.Entity.User;
import com.ankit.HealthCare_Backend.JWT.JwtService;
import com.ankit.HealthCare_Backend.Repository.AdminRepository;
import com.ankit.HealthCare_Backend.Repository.DoctorRepository;
import com.ankit.HealthCare_Backend.Repository.UserRepository;
import com.ankit.HealthCare_Backend.Service.AuthService.AuthService;
import com.ankit.HealthCare_Backend.Service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;
=======
import com.ankit.HealthCare_Backend.Entity.Doctor;
import com.ankit.HealthCare_Backend.Entity.User;
import com.ankit.HealthCare_Backend.JWT.JwtService;
import com.ankit.HealthCare_Backend.Repository.DoctorRepository;
import com.ankit.HealthCare_Backend.Repository.UserRepository;
import com.ankit.HealthCare_Backend.Service.CustomUserDetailsService;
import com.ankit.HealthCare_Backend.Service.AuthService.AuthService;
>>>>>>> origin/main

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private DoctorRepository doctorRepo;
    @Autowired
    private UserRepository userRepo;
<<<<<<< HEAD
    @Autowired
    private AdminRepository adminRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
=======
>>>>>>> origin/main

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

<<<<<<< HEAD
    // Unified login for Admin, Doctor, and Patient
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            // First check if it's an admin login
            Optional<Admin> adminOpt = adminRepo.findByEmailAndIsActive(loginRequest.getEmail(), true);
            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                
                // Verify admin password
                if (passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
                    String jwt = jwtService.generateAdminToken(admin.getEmail(), "ADMIN");
                    return ResponseEntity.ok(new LoginResponseDTO(jwt, "Admin login successful"));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponseDTO(null, "Invalid admin credentials"));
                }
            }
            
            // If not admin, proceed with normal user authentication (Doctor/Patient)
=======
    // For normal user login (Doctor/Patient)
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            // Authenticate user credentials
>>>>>>> origin/main
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    loginRequest.getPassword()
                )
            );
            
<<<<<<< HEAD
=======
            // If we get here, user is authenticated
>>>>>>> origin/main
            final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());

            // Check if it's a doctor
            boolean isDoctor = userDetails.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_DOCTOR"));

            if (isDoctor) {
                User user = userRepo.findByEmail(userDetails.getUsername());
                Doctor doctor = doctorRepo.findByUserId(user.getId());

                if (doctor != null && doctor.isApproved()) {
<<<<<<< HEAD
                    final String jwt = jwtService.generateToken(userDetails);
                    return ResponseEntity.ok(new LoginResponseDTO(jwt, "Doctor login successful"));
                } else {
=======
                    // ✅ Doctor is approved - generate token
                    final String jwt = jwtService.generateToken(userDetails);
                    return ResponseEntity.ok(new LoginResponseDTO(jwt, "Login successful"));
                } else {
                    // ❌ Doctor is not approved - return error with message field
>>>>>>> origin/main
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new LoginResponseDTO(null, "Your doctor account is not approved yet. Contact admin or wait for approval.")
                    );
                }
            } else {
<<<<<<< HEAD
                // Patient login
                final String jwt = jwtService.generateToken(userDetails);
                return ResponseEntity.ok(new LoginResponseDTO(jwt, "Patient login successful"));
            }

        } catch (BadCredentialsException e) {
=======
                // ✅ Patient or Admin - generate token directly
                final String jwt = jwtService.generateToken(userDetails);
                return ResponseEntity.ok(new LoginResponseDTO(jwt, "Login successful"));
            }

        } catch (BadCredentialsException e) {
            // Invalid email or password
>>>>>>> origin/main
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new LoginResponseDTO(null, "Invalid email or password")
            );
        } catch (Exception e) {
<<<<<<< HEAD
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new LoginResponseDTO(null, "An error occurred during login: " + e.getMessage())
            ); 
=======
            // Any other error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new LoginResponseDTO(null, "An error occurred during login: " + e.getMessage())
            );
>>>>>>> origin/main
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
<<<<<<< HEAD

    // Temporary endpoint to create admin - REMOVE IN PRODUCTION
    @PostMapping("/create-admin")
    public ResponseEntity<MessageResponseDTO> createAdmin() {
        try {
            // Check if admin already exists
            if (adminRepo.findByEmail("admin@healthcare.com").isPresent()) {
                return ResponseEntity.ok(new MessageResponseDTO("Admin already exists"));
            }
            
            Admin admin = new Admin();
            admin.setEmail("admin@healthcare.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("System Admin");
            admin.setActive(true);
            
            adminRepo.save(admin);
            
            return ResponseEntity.ok(new MessageResponseDTO("Admin created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("Error creating admin: " + e.getMessage()));
        }
    }

=======
>>>>>>> origin/main
}