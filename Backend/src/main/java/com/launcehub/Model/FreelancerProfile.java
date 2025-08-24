package com.launcehub.Model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Entity
public class FreelancerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id", nullable = false,unique = true)
    Users user;

    @NotBlank(message = "First name is required")
    String firstName;

    @NotBlank(message = "Last name is required")
    String lastName;
    
    String bio;

    @Positive(message = "Hourly rate must be a positive number")
    BigDecimal hourlyRate;

    String location;

    String[] skills;
    
    @jakarta.persistence.Column(name = "portfolio", columnDefinition = "TEXT")
    private String portfolio;

    String profilePictureUri;



}
