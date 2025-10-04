package com.ankit.HealthCare_Backend.Service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ankit.HealthCare_Backend.DTO.RegisterRequestDTO;
import com.ankit.HealthCare_Backend.DTO.UserResponseDTO;
import com.ankit.HealthCare_Backend.Entity.Role;
import com.ankit.HealthCare_Backend.Entity.User;
import com.ankit.HealthCare_Backend.Repository.RoleRepository;
import com.ankit.HealthCare_Backend.Repository.UserRepository;

@Service
public class AuthServicesImpl implements AuthService {
    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO registerUser(RegisterRequestDTO registerRequest) {
        //check for the role
        Role userRole = roleRepo.findById(registerRequest.getRoleId())
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        ;

        //create a new user entity
        User newUser=new User();
        //set the email to the new user entity(user)
        newUser.setEmail(registerRequest.getEmail());

        //set the password with encoding 
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        //set the Role of the new user
        newUser.setRole(userRole);


        //now save the new user Entity to the DB
        User savedUser=userRepo.save(newUser);

        //Create the response DTO
        UserResponseDTO response=new UserResponseDTO();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setRoleName(savedUser.getRole().getName()); //set the name of the Role 


        //return response DTO
        return response;
    }
}
