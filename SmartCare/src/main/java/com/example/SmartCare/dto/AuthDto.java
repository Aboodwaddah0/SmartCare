package com.example.SmartCare.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class AuthDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    public static  class  LoginRequest {
        @NotBlank(message = "Username cannot be blank")
        private String username;
        @NotBlank(message = "Password cannot be blank")
        private String password;
    }
}
