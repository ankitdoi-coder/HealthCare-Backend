package com.ankit.HealthCare_Backend.DTO;

import lombok.Data;

//this is the Type of Input we will take from the user Like Empty Object Type of this
@Data
public class RegisterRequestDTO {
    private String email;
    private String password;
    private Long roleId;
}