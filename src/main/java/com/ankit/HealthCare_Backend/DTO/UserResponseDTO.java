package com.ankit.HealthCare_Backend.DTO;

import lombok.Data;

//this is the response for Rest Api "Without Password"
@Data
public class UserResponseDTO {
    private Long id;
    private String email;
    private String roleName;
}
