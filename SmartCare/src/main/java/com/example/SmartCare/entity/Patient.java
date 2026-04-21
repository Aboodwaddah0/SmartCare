package com.example.SmartCare.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
public class Patient extends User {

    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String age;
    @Column(nullable = false)
    private String gender;
}
