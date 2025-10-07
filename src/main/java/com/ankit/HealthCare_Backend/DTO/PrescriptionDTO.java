package com.ankit.HealthCare_Backend.DTO;

import lombok.Data;

@Data
public class PrescriptionDTO {
    private Long id;
    private Long appointmentId;
    private String medicationDetails;
    private String dosages;
}
