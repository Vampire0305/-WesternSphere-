package com.jobportal.JobPortal.controller;

import com.jobportal.JobPortal.DTO.*;
import com.jobportal.JobPortal.repository.UserRepository;
import com.jobportal.JobPortal.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registration request received for email: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request received for email: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        log.info("Token refresh request received");
        String token = refreshToken.startsWith("Bearer ") ?
                refreshToken.substring(7) : refreshToken;
        AuthResponse response = authService.refreshToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("Forgot password request received for email: {}", request.getEmail());
        authService.forgotPassword(request);
        return ResponseEntity.ok("If the email exists, a password reset link has been sent.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        log.info("Reset password request received");
        authService.resetPassword(request);
        return ResponseEntity.ok("Password reset successful.");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {
        log.info("Change password request received for user: {}", authentication.getName());
        authService.changePassword(authentication.getName(), request);
        return ResponseEntity.ok("Password changed successfully.");
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        log.info("Email verification request received");
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        log.info("Logout request received");
        String jwtToken = token.startsWith("Bearer ") ?
                token.substring(7) : token;
        authService.logout(jwtToken);
        return ResponseEntity.ok("Logout successful.");
    }

    @PostMapping("/get-email-token")
    public ResponseEntity<String> getNewEmailToken(@RequestHeader("Authorization") String refreshToken) {
        log.info("Token refresh request received");
        String token = refreshToken.startsWith("Bearer ") ?
                refreshToken.substring(7) : refreshToken;
        authService.getNewEmailToken(token);
        return ResponseEntity.ok("Email sent to your email");
    }
    @GetMapping("/internal/count")
    @PreAuthorize("hasRole('ADMIN')") // Only users with the ADMIN role can access this
    public ResponseEntity<Long> countInternal() {
        try {
            Long count = userRepository.count();
            // Return a 200 OK status with the count in the body
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            // Handle any database or repository exceptions gracefully
            // Return a 500 Internal Server Error with a more informative message
            // You can also log the exception here for debugging
            System.err.println("Error while counting users: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L); // Or just return 0
        }
    }


}
