package com.ankit.HealthCare_Backend.Service.DoctorService;

import java.util.List;

import com.ankit.HealthCare_Backend.DTO.AppointmentDTO;
import com.ankit.HealthCare_Backend.DTO.PrescriptionDTO;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface DoctorService {
    List<AppointmentDTO> myUpcomingAppointments();
    PrescriptionDTO createPrescription(@RequestBody PrescriptionDTO prescriptionDTO);
}
