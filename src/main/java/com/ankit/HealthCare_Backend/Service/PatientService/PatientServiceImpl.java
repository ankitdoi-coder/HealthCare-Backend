package com.ankit.HealthCare_Backend.Service.PatientService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ankit.HealthCare_Backend.DTO.AppointmentDTO;
import com.ankit.HealthCare_Backend.DTO.DoctorDTO;
import com.ankit.HealthCare_Backend.Entity.Appointment;
import com.ankit.HealthCare_Backend.Entity.Doctor;
import com.ankit.HealthCare_Backend.Entity.Patient;
import com.ankit.HealthCare_Backend.Enums.AppointmentStatus;
import com.ankit.HealthCare_Backend.Repository.AppointmentRepository;
import com.ankit.HealthCare_Backend.Repository.DoctorRepository;
import com.ankit.HealthCare_Backend.Repository.PatientRepository;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    DoctorRepository doctorRepo;

    @Autowired
    AppointmentRepository appointmentRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Override
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepo.findAll()
                .stream()
                // .filter(Doctor::isApproved) // Only show approved doctors!
                .map(this::convertToDoctorDto)
                .collect(Collectors.toList());
    }

    // Convert Doctor Entity Data to DoctorDTO Data
    private DoctorDTO convertToDoctorDto(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setSpecialty(doctor.getSpecialty());
        dto.setApproved(doctor.isApproved());

        // Add other fields as needed
        return dto;
    }

    
    @Override
    @Transactional
    public AppointmentDTO newAppointment(AppointmentDTO appointmentDTO) {
        // 1. Find the actual Patient and Doctor entities using the IDs from the DTO
        Patient patient = patientRepo.findById(appointmentDTO.getPatientId())
            .orElseThrow(() -> new RuntimeException("Patient not found with id: " + appointmentDTO.getPatientId()));
            
        Doctor doctor = doctorRepo.findById(appointmentDTO.getDoctorId())
            .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + appointmentDTO.getDoctorId()));

        // 2. Now, create the real Appointment entity using the full objects
        Appointment newAppointment = new Appointment();
        newAppointment.setPatient(patient);
        newAppointment.setDoctor(doctor);
        newAppointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
        newAppointment.setStatus(AppointmentStatus.SCHEDULED); // Set a default status

        // 3. Save the new appointment
        Appointment savedAppointment = appointmentRepo.save(newAppointment);

        // 4. Convert the saved entity back to a DTO to send to the client
        return convertToAppointmentDto(savedAppointment);
    }

    private AppointmentDTO convertToAppointmentDto(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        // Get the IDs from the objects to put in the DTO
        dto.setPatientId(appointment.getPatient().getId());
        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setStatus(appointment.getStatus());
        return dto;
    }
}
