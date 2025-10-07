package com.ankit.HealthCare_Backend.Service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ankit.HealthCare_Backend.DTO.RegisterRequestDTO;
import com.ankit.HealthCare_Backend.DTO.UserResponseDTO;
import com.ankit.HealthCare_Backend.Entity.Doctor;
import com.ankit.HealthCare_Backend.Entity.Patient;
import com.ankit.HealthCare_Backend.Entity.Role;
import com.ankit.HealthCare_Backend.Entity.User;
import com.ankit.HealthCare_Backend.Repository.DoctorRepository;
import com.ankit.HealthCare_Backend.Repository.PatientRepository;
import com.ankit.HealthCare_Backend.Repository.RoleRepository;
import com.ankit.HealthCare_Backend.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthServicesImpl implements AuthService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private DoctorRepository doctorRepo;
    @Autowired
    private PatientRepository patientRepo;

    @Override
    @Transactional
    public UserResponseDTO registerUser(RegisterRequestDTO registerRequest) {
        Role userRole = roleRepo.findById(registerRequest.getRoleId())
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        // Part 1: Create the User record
        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setRole(userRole);
        newUser.setApproved("PATIENT".equalsIgnoreCase(userRole.getName()));
        User savedUser = userRepo.save(newUser);

        // Create a basic response object first
        UserResponseDTO response = new UserResponseDTO();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setRoleName(savedUser.getRole().getName());
        
        // Part 2: Create the Doctor or Patient profile and add details to the response
        if ("DOCTOR".equalsIgnoreCase(userRole.getName())) {
            Doctor newDoctor = new Doctor();
            newDoctor.setFirstName(registerRequest.getFirstName());
            newDoctor.setLastName(registerRequest.getLastName());
            newDoctor.setSpecialty(registerRequest.getSpecialty());
            newDoctor.setUser(savedUser);
            newDoctor.setApproved(false);
            doctorRepo.save(newDoctor);

            // Add doctor-specific details to the response
            response.setFirstName(newDoctor.getFirstName());
            response.setLastName(newDoctor.getLastName());
            response.setSpecialty(newDoctor.getSpecialty());
            response.setMessage("Approval request sent to admin!");

        } else if ("PATIENT".equalsIgnoreCase(userRole.getName())) {
            Patient newPatient = new Patient();
            newPatient.setFirstName(registerRequest.getFirstName());
            newPatient.setLastName(registerRequest.getLastName());
            newPatient.setContactNumber(registerRequest.getContactNumber());
            newPatient.setDob(registerRequest.getDob());
            newPatient.setUser(savedUser);
            patientRepo.save(newPatient);

            // Add patient-specific details to the response
            response.setFirstName(newPatient.getFirstName());
            response.setLastName(newPatient.getLastName());
            response.setContactNumber(newPatient.getContactNumber());
            response.setDob(newPatient.getDob());
            response.setMessage("Registration successful!");
        }

        // ✅ This is the final, guaranteed return statement
        return response;
    }
}
