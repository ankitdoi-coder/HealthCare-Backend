package com.ankit.HealthCare_Backend.DTO;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
    // private String loginType; // "admin", "user" - optional field to distinguish login types
}
