//contains secret key its like a drower for application.properties
package com.ankit.HealthCare_Backend.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
<<<<<<< HEAD
=======
import org.springframework.stereotype.Component;
>>>>>>> origin/main

import lombok.Data;
import lombok.Getter;

<<<<<<< HEAD
=======
@Component
>>>>>>> origin/main
@ConfigurationProperties(prefix = "app")
@Getter
public class AppProperties {

    private final Jwt jwt = new Jwt();
<<<<<<< HEAD

=======
    private final Admin admin = new Admin();

    
>>>>>>> origin/main
    // Nested classes for organization
    @Data
    public static class Jwt {
        private String secret;
    }
<<<<<<< HEAD
=======

    //for admin
    @Data
    public static class Admin {
        private String email;
        private String password;

        
    }
>>>>>>> origin/main
}
