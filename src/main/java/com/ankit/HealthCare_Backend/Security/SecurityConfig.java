package com.ankit.HealthCare_Backend.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF, which is not needed for stateless REST APIs
                .csrf(csrf -> csrf.disable())

                // 2. Define authorization rules
                .authorizeHttpRequests(auth -> auth
                        // 🎯 THIS IS THE MOST IMPORTANT PART
                        // Allow public access to all URLs under /api/auth/ (like /register, /login)
                        .requestMatchers("/api/auth/**").permitAll()

                        // Secure other endpoints (example for a doctor-specific URL)
                        .requestMatchers("/api/doctor/**").hasRole("DOCTOR")

                        // Require authentication for any other request that is not explicitly permitted
                        .anyRequest().authenticated())

                // 3. Set session management to STATELESS for JWT/token-based auth
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // If you have a JWT filter, you would add it here using .addFilterBefore()

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
