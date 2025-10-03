package com.ankit.HealthCare_Backend.Service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ankit.HealthCare_Backend.Entity.Role;
import com.ankit.HealthCare_Backend.Entity.User;
import com.ankit.HealthCare_Backend.Repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo; // Changed to private for better encapsulation

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // ✅ CORRECTED LOGIC
        // Step 1: Get the entire Role object from the user.
        Role roleObject = user.getRole();

        // Step 2: Get the role name (a String) from the Role object.
        // We assume your Role entity has a field named 'name' or 'roleName'. Let's use
        // 'name'.
        String roleName = roleObject.getName();

        // Null check for safety
        if (roleName == null || roleName.trim().isEmpty()) {
            throw new IllegalStateException("User '" + email + "' has a role with no name.");
        }

        // Step 3: Use the roleName string to create the authority.
        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}