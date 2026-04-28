package com.example.SmartCare.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "doctor")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String specialty;

    private String experience;

    private String profilePic;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DoctorSchedule> schedules = new ArrayList<>();


}