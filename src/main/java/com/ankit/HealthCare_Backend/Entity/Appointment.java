package com.ankit.HealthCare_Backend.Entity;

import java.util.Date;

import com.ankit.HealthCare_Backend.Enums.AppointmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="Appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Patient Id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Doctor Id", nullable = false)
    private Doctor doctor;

    @Column(name = "Appointment Date", nullable = false)
    private Date appointment_date;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AppointmentStatus status; // Enum: SCHEDULED, COMPLETED, CANCELED
}
