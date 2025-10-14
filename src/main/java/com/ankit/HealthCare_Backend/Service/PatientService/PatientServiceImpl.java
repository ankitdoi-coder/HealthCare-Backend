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
import com.ankit.HealthCare_Backend.DTO.PatientDTO;
import com.ankit.HealthCare_Backend.DTO.PrescriptionDTO;
import com.ankit.HealthCare_Backend.Entity.Appointment;
import com.ankit.HealthCare_Backend.Entity.Doctor;
import com.ankit.HealthCare_Backend.Entity.Patient;
import com.ankit.HealthCare_Backend.Entity.Prescription;
import com.ankit.HealthCare_Backend.Entity.User;
import com.ankit.HealthCare_Backend.Enums.AppointmentStatus;
import com.ankit.HealthCare_Backend.Repository.AppointmentRepository;
import com.ankit.HealthCare_Backend.Repository.DoctorRepository;
import com.ankit.HealthCare_Backend.Repository.PatientRepository;
import com.ankit.HealthCare_Backend.Repository.PrescriptionRepository;
import com.ankit.HealthCare_Backend.Repository.UserRepository;
import com.ankit.HealthCare_Backend.Repository.BillingRepository;
import com.ankit.HealthCare_Backend.Entity.Billing;
import com.ankit.HealthCare_Backend.Enums.BillingStatus;

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

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private BillingRepository billingRepo;

    //Get All Doctors
    @Override
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepo.findAll()
                .stream()
                .filter(Doctor::isApproved) // Only show approved doctors!
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
        dto.setEmail(doctor.getUser().getEmail());
        dto.setExperience(doctor.getExperience());
        // Add other fields as needed
        return dto;
    }

    private AppointmentDTO convertToAppointmentDto(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setPatientId(appointment.getPatient().getId());
        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setDoctorFirstName(appointment.getDoctor().getFirstName());
        dto.setDoctorLastName(appointment.getDoctor().getLastName());
        dto.setDoctorSpecialty(appointment.getDoctor().getSpecialty());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setStatus(appointment.getStatus());
        
        // Get billing info
        Billing billing = billingRepo.findAll().stream()
                .filter(b -> b.getAppointment_id().getId().equals(appointment.getId()))
                .findFirst().orElse(null);
        
        if (billing != null) {
            dto.setBillingStatus(billing.getBilling_status());
            dto.setAmount(billing.getAmount());
        } else {
            dto.setBillingStatus(BillingStatus.UNPAID);
            dto.setAmount(500);
        }
        
        return dto;
    }

    // newAppointment
    @Override
    @Transactional
    public AppointmentDTO newAppointment(AppointmentDTO appointmentDTO, String patientEmail) {
        // 1. Find patient by email
        User user = userRepo.findByEmail(patientEmail);
        if (user == null) {
            throw new RuntimeException("User not found: " + patientEmail);
        }

        Patient patient = patientRepo.findByUserId(user.getId());
        if (patient == null) {
            throw new RuntimeException("No patient profile found for user: " + patientEmail);
        }

        Doctor doctor = doctorRepo.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + appointmentDTO.getDoctorId()));

        // 2. Create the appointment entity
        Appointment newAppointment = new Appointment();
        newAppointment.setPatient(patient);
        newAppointment.setDoctor(doctor);
        newAppointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
        newAppointment.setStatus(AppointmentStatus.PENDING);

        // 3. Save the new appointment
        Appointment savedAppointment = appointmentRepo.save(newAppointment);

        // 4. Convert back to DTO
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

    @Override
    public List<PrescriptionDTO> getMyPrescriptions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("Unauthenticated");
        }

        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found: " + email);
        }

        Patient patient = patientRepo.findByUserId(user.getId());
        if (patient == null) {
            throw new RuntimeException("No patient profile found for user: " + email);
        }

        List<Appointment> appointments = appointmentRepo.findByPatientId(patient.getId());
        return appointments.stream()
                .flatMap(appointment -> prescriptionRepo.findAll().stream()
                        .filter(prescription -> prescription.getAppointment().getId().equals(appointment.getId())))
                .map(this::convertToPrescriptionDto)
                .collect(Collectors.toList());
    }

    private PrescriptionDTO convertToPrescriptionDto(Prescription prescription) {
        PrescriptionDTO dto = new PrescriptionDTO();
        dto.setId(prescription.getId());
        dto.setAppointmentId(prescription.getAppointment().getId());
        dto.setMedicationDetails(prescription.getMedicationDetails());
        dto.setDosages(prescription.getDosages());
        return dto;
    }

    @Override
    public PatientDTO getMyProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("Unauthenticated");
        }

        String email = auth.getName();
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found: " + email);
        }

        Patient patient = patientRepo.findByUserId(user.getId());
        if (patient == null) {
            throw new RuntimeException("No patient profile found for user: " + email);
        }

        return convertToPatientDto(patient);
    }

    @Override
    @Transactional
    public void makePayment(Long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        Billing billing = billingRepo.findAll().stream()
                .filter(b -> b.getAppointment_id().getId().equals(appointmentId))
                .findFirst().orElse(null);
        
        if (billing == null) {
            billing = new Billing();
            billing.setAppointment_id(appointment);
            billing.setAmount(500);
            billing.setBilling_status(BillingStatus.PAID);
        } else {
            billing.setBilling_status(BillingStatus.PAID);
        }
        
        billingRepo.save(billing);
    }

    @Override
    @Transactional
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = appointmentRepo.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        
        // Delete the appointment
        appointmentRepo.delete(appointment);
        
        // Also delete associated billing if exists
        billingRepo.findAll().stream()
                .filter(b -> b.getAppointment_id().getId().equals(appointmentId))
                .findFirst()
                .ifPresent(billingRepo::delete);
    }

    private PatientDTO convertToPatientDto(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setUserId(patient.getUser().getId());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setContactNumber(patient.getContactNumber());
        dto.setDob(patient.getDob());
        return dto;
    }

}
