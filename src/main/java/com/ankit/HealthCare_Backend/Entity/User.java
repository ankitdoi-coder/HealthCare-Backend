package com.ankit.HealthCare_Backend.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,name ="Email")
    private String email;

    @Column(nullable = false, name = "Password")
    private String password;


    //Many users can be assosiated with one Role
    @ManyToOne(fetch = FetchType.LAZY)         
    @JoinColumn(name = "role_id",nullable = false)
    private Role role;
}
