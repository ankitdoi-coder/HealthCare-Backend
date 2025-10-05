package com.ankit.HealthCare_Backend.Service.PatientService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ankit.HealthCare_Backend.DTO.DoctorDTO;
import com.ankit.HealthCare_Backend.Entity.Doctor;
import com.ankit.HealthCare_Backend.Repository.DoctorRepository;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    DoctorRepository doctorRepo;

    @Override
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepo.findAll()
                .stream()
                // .filter(Doctor::isApproved) // Only show approved doctors!
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private DoctorDTO convertToDto(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setSpecialty(doctor.getSpecialty());
        dto.setApproved(doctor.isApproved());
        
        // Add other fields as needed
        return dto;
    }

}
