package com.ankit.HealthCare_Backend.Security;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = "/"; // default

        for (GrantedAuthority ga : authorities) {
            String role = ga.getAuthority();
            if (role.equals("ROLE_PATIENT")) {
                redirectUrl = "/patient/dashboard";
                break;
            } else if (role.equals("ROLE_DOCTOR")) {
                redirectUrl = "/doctor/dashboard";
                break;
            } else if (role.equals("ROLE_ADMIN")) {
                // Admins shouldn't login via usual flow in this app; but if they do, send to admin page
                redirectUrl = "/admin/manage";
                break;
            }
        }

        response.setStatus(HttpServletResponse.SC_FOUND);
        response.sendRedirect(redirectUrl);
    }
}
