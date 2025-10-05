package com.ankit.HealthCare_Backend.Service.PatientService;

import java.util.List;

import com.ankit.HealthCare_Backend.DTO.DoctorDTO;

public interface PatientService {
    List<DoctorDTO> getAllDoctors();
}
