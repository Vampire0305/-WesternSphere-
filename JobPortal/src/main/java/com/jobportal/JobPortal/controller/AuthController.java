package com.jobportal.JobPortal.controller;


import com.jobportal.JobPortal.DTO.AuthResponse;
import com.jobportal.JobPortal.DTO.LoginRequest;
import com.jobportal.JobPortal.DTO.RegisterRequest;
import com.jobportal.JobPortal.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        ResponseEntity<AuthResponse> hello= ResponseEntity.ok(authService.register(request));
        return hello;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

}

