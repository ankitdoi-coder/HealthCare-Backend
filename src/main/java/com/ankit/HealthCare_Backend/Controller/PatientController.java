package com.ankit.HealthCare_Backend.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PatientController {
    
    //gets the List of the doctors
    @GetMapping("/doctors")
    public void getDoctors(){

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
