//contains secret key its like a drower for application.properties
package com.ankit.HealthCare_Backend.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.Getter;

@ConfigurationProperties(prefix = "app")
@Getter
public class AppProperties {

    private final Jwt jwt = new Jwt();
    private final Admin admin = new Admin();

    
    // Nested classes for organization
    @Data
    public static class Jwt {
        private String secret;
    }

    //for admin
    @Data
    public static class Admin {
        private String email;
        private String password;

        
    }
}
