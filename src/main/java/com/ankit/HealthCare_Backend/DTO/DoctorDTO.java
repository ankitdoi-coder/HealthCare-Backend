// In your DTO package
package com.ankit.HealthCare_Backend.DTO;

import lombok.Data;

@Data
public class DoctorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialty;
    private boolean isApproved;
}