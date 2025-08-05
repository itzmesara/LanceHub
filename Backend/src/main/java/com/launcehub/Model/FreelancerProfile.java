package com.launcehub.Model;

import java.math.BigDecimal;
import java.sql.Types;

import org.hibernate.annotations.Type;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class FreelancerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id", nullable = false,unique = true)
    Users user;

    String firstName;
    String lastName;
    String bio;

    BigDecimal hourlyRate;

    String location;

    String[] skills;
    
    @Type(type = "org.hibernate.type.TextType")
    @Column(columnDefinition = "text")
    Object portfolio;


}
