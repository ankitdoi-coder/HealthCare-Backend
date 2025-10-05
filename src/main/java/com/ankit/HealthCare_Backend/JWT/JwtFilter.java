//The "Wristband Checker" Filter (JwtFilter.java)
//This is a special guard who stands in front of every endpoint.
//Their only job is to check for a valid wristband on every request.
package com.ankit.HealthCare_Backend.JWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @org.springframework.lang.NonNull HttpServletRequest request,
            @org.springframework.lang.NonNull HttpServletResponse response,
            @org.springframework.lang.NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // matches the authorization header with the token
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Cuts off the "Bearer " part and keeps only the actual token.
        final String jwt = authHeader.substring(7);

        // Uses the JwtService to read the sub claim (username/email) from the token.
        final String userEmail = jwtService.extractUsername(jwt);

        // Two checks before authenticating
        // Is there a username inside the token?
        // Has Spring Security already authenticated this request? (avoid duplicate
        // work)
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Loads full user info (password hash, roles, authorities) from DB by
            // email/username.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Finally, hand over control to the next filter or the actual controller.
        filterChain.doFilter(request, response);
    }
}
