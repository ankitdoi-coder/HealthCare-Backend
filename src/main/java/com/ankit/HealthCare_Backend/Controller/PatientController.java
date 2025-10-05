package com.ankit.HealthCare_Backend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;

import com.ankit.HealthCare_Backend.DTO.DoctorDTO;
import com.ankit.HealthCare_Backend.Entity.Doctor;
import com.ankit.HealthCare_Backend.Service.PatientService.PatientService;

@RestController
@RequestMapping("/api/patient")
public class PatientController {
    @Autowired
    PatientService patientService;


    //gets the List of the doctors
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDTO>> getDoctors(){
        List<DoctorDTO> doctors =patientService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    //Book a new Appointment
    @PostMapping("/appointments")
    public void newAppointment(){

    }

    //View personal appointment history
    @GetMapping("/appointments/my")
    public void myAppointments(){
        
    }
}
