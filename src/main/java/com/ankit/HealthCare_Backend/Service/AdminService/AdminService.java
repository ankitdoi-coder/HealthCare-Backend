package com.ankit.HealthCare_Backend.Service.AdminService;

import java.util.List;
import com.ankit.HealthCare_Backend.DTO.DoctorDTO;
import com.ankit.HealthCare_Backend.DTO.PatientDTO;

public interface AdminService {
    List<DoctorDTO> getAllDoctors();
    DoctorDTO approveDoctor(Long id);
    List<PatientDTO> getAllPatients();
}
