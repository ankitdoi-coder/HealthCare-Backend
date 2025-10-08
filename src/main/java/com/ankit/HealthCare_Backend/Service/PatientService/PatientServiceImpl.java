package com.ankit.HealthCare_Backend.Service.PatientService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.Authentication;
import com.ankit.HealthCare_Backend.DTO.AppointmentDTO;
import com.ankit.HealthCare_Backend.DTO.DoctorDTO;
import com.ankit.HealthCare_Backend.Entity.Appointment;
import com.ankit.HealthCare_Backend.Entity.Doctor;
import com.ankit.HealthCare_Backend.Entity.Patient;
import com.ankit.HealthCare_Backend.Entity.User;
import com.ankit.HealthCare_Backend.Enums.AppointmentStatus;
import com.ankit.HealthCare_Backend.Repository.AppointmentRepository;
import com.ankit.HealthCare_Backend.Repository.DoctorRepository;
import com.ankit.HealthCare_Backend.Repository.PatientRepository;
import com.ankit.HealthCare_Backend.Repository.UserRepository;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    DoctorRepository doctorRepo;

    @Autowired
    AppointmentRepository appointmentRepo;

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private UserRepository userRepo;

    // Alldoctors
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

    // newAppointment
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

    // My appointments
    @Override
    public List<AppointmentDTO> getMyAppointments() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("Unauthenticated");
        }

        String email = auth.getName(); // your CustomUserDetailsService sets the username = email

        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found: " + email);
        }

        // The 'User' table and 'Patient' table are separate. We need the Patient record
        // that links to this User (patient.user_id -> users.id) and then query
        // appointments
        Patient patient = patientRepo.findByUserId(user.getId());
        if (patient == null) {
            // The authenticated user exists but is not a patient (or patient profile
            // missing)
            throw new RuntimeException("No patient profile found for user: " + email);
        }

        Long patientId = patient.getId();
        List<Appointment> appointments = appointmentRepo.findByPatientId(patientId);

        return appointments.stream()
                .map(appointment -> convertToAppointmentDto(appointment))
                .collect(Collectors.toList());
    }

}
