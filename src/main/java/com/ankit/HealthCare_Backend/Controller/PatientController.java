package com.ankit.HealthCare_Backend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;

import com.ankit.HealthCare_Backend.DTO.AppointmentDTO;
import com.ankit.HealthCare_Backend.DTO.DoctorDTO;
import com.ankit.HealthCare_Backend.Service.PatientService.PatientService;

import org.springframework.web.bind.annotation.RequestBody;

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
    @PostMapping("/appointments/new")
    public ResponseEntity<AppointmentDTO> newAppointment(@RequestBody AppointmentDTO appointmentDTO){
        AppointmentDTO newAppointment = patientService.newAppointment(appointmentDTO);
        return ResponseEntity.ok(newAppointment);

    }

    //View personal appointment history
    @GetMapping("/appointments/my")
    public ResponseEntity<List<AppointmentDTO>> myAppointments(){
        List<AppointmentDTO> appointments = patientService.getMyAppointments();
        return ResponseEntity.ok(appointments);
    }
}
