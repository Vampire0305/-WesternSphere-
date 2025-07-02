package com.jobportal.JobPortal.service;


import com.jobportal.JobPortal.DTO.AuthResponse;
import com.jobportal.JobPortal.DTO.LoginRequest;
import com.jobportal.JobPortal.DTO.RegisterRequest;
import com.jobportal.JobPortal.entity.User;
import com.jobportal.JobPortal.repository.UserRepository;
import com.jobportal.JobPortal.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtil jwtUtil;


    public AuthResponse register(RegisterRequest request) {
        User user = new User();
        user.setName(request.name);
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setRole(request.role);
        userRepository.save(user);

        String token = jwtUtil.generateToken( user.getEmail(),user.getRole().name());
        return  new AuthResponse(token,"User Registerd Successful");


    }


    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email).orElseThrow(() -> new RuntimeException("User not found"));


        if(!passwordEncoder.matches(request.password, user.getPassword()))
            throw new RuntimeException("Invalid credentials");

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token,"Login Successful");


    }
}

