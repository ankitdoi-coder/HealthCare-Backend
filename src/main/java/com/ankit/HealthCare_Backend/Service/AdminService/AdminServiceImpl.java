package com.ankit.HealthCare_Backend.Service.AdminService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ankit.HealthCare_Backend.DTO.DoctorDTO;
import com.ankit.HealthCare_Backend.DTO.PatientDTO;
import com.ankit.HealthCare_Backend.Entity.Doctor;
import com.ankit.HealthCare_Backend.Entity.Patient;
import com.ankit.HealthCare_Backend.Repository.DoctorRepository;
import com.ankit.HealthCare_Backend.Repository.PatientRepository;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private DoctorRepository doctorRepo;
    @Autowired
    private PatientRepository patientRepo;

    // get the list of the doctors
    @Override
    public List<DoctorDTO> getAllDoctors() {
        List<Doctor> doctors = doctorRepo.findAll();
         return doctors.stream()
                .map(appointment -> convertToDoctorDto(appointment))
                .collect(Collectors.toList());
    }

    // approve Doctor
    @Override
    @Transactional // Make sure the operation is transactional
    public DoctorDTO approveDoctor(Long id) {
        // Find the doctor, or throw an exception if not found
        Doctor doctor = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        // Update the doctor's status
        doctor.setApproved(true);

        // Save the updated doctor (optional if inside a @Transactional method, but good
        // practice)
        Doctor savedDoctor = doctorRepo.save(doctor);

        // Convert the updated and saved doctor to a DTO and return it
        return convertToDoctorDto(savedDoctor);
    }

    // get all the patient
    @Override
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepo.findAll();
         return patients.stream()
                .map(appointment -> convertToPatientDto(appointment))
                .collect(Collectors.toList());
    }

    // helper method
    DoctorDTO convertToDoctorDto(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setSpecialty(doctor.getSpecialty());
        dto.setApproved(doctor.isApproved());
        return dto;
    }

    PatientDTO convertToPatientDto(Patient patient) {
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
