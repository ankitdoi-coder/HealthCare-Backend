package com.ankit.HealthCare_Backend.Entity;

import com.ankit.HealthCare_Backend.Enums.BillingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Appointment Id",nullable = false)
    private Appointment appointment_id;
    
    @Column(name = "Amount",nullable = false)
    private int amount;

    @Column(name = "Billing Status",nullable = false)
    private BillingStatus billing_status;   
}
