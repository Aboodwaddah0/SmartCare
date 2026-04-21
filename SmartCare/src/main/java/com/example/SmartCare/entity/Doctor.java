package com.example.SmartCare.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
public class Doctor extends User {

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private String experience;

    private String profilePic;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String availability;




}
