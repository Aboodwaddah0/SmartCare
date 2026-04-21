package com.example.SmartCare.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;

public class UserDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class CreatePatientRequest {

        @NotBlank(message = "Name cannot be blank")
        private String fullName;
        @NotBlank(message = "Username cannot be blank")
        private String username;
        @NotBlank(message = "Password cannot be blank")
        private String password;
        @NotBlank(message = "Email cannot be blank")
        private String email;
        @NotBlank(message = "Phone cannot be blank")
        private String phone;
        @NotBlank(message = "Age cannot be blank")
        private String age;
        @NotBlank(message = "Gender cannot be blank")
        private String gender;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static class CreateDoctorRequest {

        @NotBlank(message = "Name cannot be blank")
        private String fullName;
        @NotBlank(message = "Username cannot be blank")
        private String username;
        @NotBlank(message = "Password cannot be blank")
        private String password;
        @NotBlank(message = "Email cannot be blank")
        private String email;
        @NotBlank(message = "Phone cannot be blank")
        private String phone;
        @NotBlank(message = "Specialization cannot be blank")
        private String specialization;
        private String experience;
        private String profilePic;

    }
}