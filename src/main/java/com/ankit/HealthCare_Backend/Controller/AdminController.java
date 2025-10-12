package com.ankit.HealthCare_Backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ankit.HealthCare_Backend.DTO.DoctorDTO;
import com.ankit.HealthCare_Backend.DTO.PatientDTO;
import com.ankit.HealthCare_Backend.Service.AdminService.AdminService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    // doctors list
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        // 1. Give the list a variable name, like "doctors"
        List<DoctorDTO> doctors = adminService.getAllDoctors();

        // 2. Return the list inside a ResponseEntity
        return ResponseEntity.ok(doctors);
    }

    // to approve
    @PutMapping("/doctors/{id}/approve")
    public ResponseEntity<DoctorDTO> approveDoctor(@PathVariable Long id) {
        DoctorDTO approvedDoctor = adminService.approveDoctor(id);

        return ResponseEntity.ok(approvedDoctor);
    }

    // patient list
    @GetMapping("/patients")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = adminService.getAllPatients();
        return ResponseEntity.ok(patients);
    }
}
