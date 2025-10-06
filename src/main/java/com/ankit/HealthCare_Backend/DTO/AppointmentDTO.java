// In AppointmentDTO.java
package com.ankit.HealthCare_Backend.DTO;

import com.ankit.HealthCare_Backend.Enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDate;


@Data
public class AppointmentDTO {
    private Long id;

    private Long patientId;
    private Long doctorId;

    private LocalDate appointmentDate;

    private AppointmentStatus status;
}