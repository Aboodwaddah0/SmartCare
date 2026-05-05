package com.example.SmartCare.controller;

import com.example.SmartCare.dto.AuthDto;
import com.example.SmartCare.dto.ApiResponse;
import com.example.SmartCare.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController  {

     private  final  AuthService authService;



    public AuthController(AuthService authService) {
        this.authService = authService;
    }
@PostMapping("/login")
    public ResponseEntity<ApiResponse> login ( @RequestBody  AuthDto.LoginRequest request){
      return ResponseEntity.ok(ApiResponse.success(authService.login(request)));
   }


}
