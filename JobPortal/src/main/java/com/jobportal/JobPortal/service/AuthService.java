package com.jobportal.JobPortal.service;

import com.jobportal.JobPortal.DTO.*;
import com.jobportal.JobPortal.Enum.Role;
import com.jobportal.JobPortal.entity.Student;
import com.jobportal.JobPortal.entity.User;
import com.jobportal.JobPortal.repository.StudentRepository;
import com.jobportal.JobPortal.repository.UserRepository;
import com.jobportal.JobPortal.security.JWTUtil;
import com.jobportal.JobPortal.exception.AuthenticationException;
import com.jobportal.JobPortal.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final EmailService emailService;
    private final StudentService studentService;
    private final RecruiterService recruiterService;
    private final AdminUserService adminUserService;

    @Value("${app.security.max-failed-attempts:5}")
    private int maxFailedAttempts;

    @Value("${app.security.account-lock-duration:30}")
    private int accountLockDurationMinutes;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());

        // Validate passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("Passwords do not match");
        }

        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ValidationException("Email already registered");
        }

        // Validate role
        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid role specified");
        }

        // Create user
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .emailVerificationToken(UUID.randomUUID().toString())
                .emailVerificationExpiry(LocalDateTime.now().plusHours(24))
                .build();

        User savedUser = userRepository.save(user);


        if(role==Role.STUDENT && request.getStudentProfile() != null) {
            studentService.createStudent(request.getStudentProfile(),savedUser);
        } else if (role == Role.RECRUITER && request.getRecruiterProfile() != null) {
            recruiterService.createRecruiter(request.getRecruiterProfile(),savedUser);
        } else if (role ==Role.ADMIN && request.getAdminUserProfile() != null) {
            adminUserService.createAdmin(request.getAdminUserProfile(),savedUser);

        } else {
            return AuthResponse.builder().message("Error getting "+role.name()+" information !!! TRY AGAIN").build();
        }

        log.info("User registered successfully with id: {}", savedUser.getId());

        // Generate tokens
        String token = jwtUtil.generateToken(savedUser.getId(),savedUser.getEmail(), savedUser.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(savedUser.getId(),savedUser.getEmail(),savedUser.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .expiresIn(jwtUtil.getExpirationTime())
                .message("User registered successfully. Please verify your email.")
                .userInfo(AuthResponse.UserInfo.builder()
                        .id(savedUser.getId())
                        .name(savedUser.getName())
                        .email(savedUser.getEmail())
                        .role(savedUser.getRole().name())
                        .isEmailVerified(savedUser.getIsEmailVerified())
                        .build())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        log.info("Attempting login for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        // Check if account is locked
        if (user.isAccountLocked()) {
            throw new AuthenticationException("Account is temporarily locked. Please try again later.");
        }

        // Check if account is active
        if (!user.getIsActive()) {
            throw new AuthenticationException("Account is deactivated. Please contact support.");
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            handleFailedLogin(user);
            throw new AuthenticationException("Invalid credentials");
        }

        // Reset failed attempts and update last login
        user.setFailedLoginAttempts(0);
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("Login successful for user: {}", user.getEmail());

        // Generate tokens  ( TODO : change this later )
        String token = jwtUtil.generateToken(user.getId(),user.getEmail(), user.getRole().name());
        String refreshToken = request.getRememberMe() ?
                jwtUtil.generateRefreshToken(user.getId(),user.getEmail(),user.getRole().name()) :
                jwtUtil.generateRefreshToken(user.getId(),user.getEmail(),user.getRole().name());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .expiresIn(jwtUtil.getExpirationTime())
                .message("Login successful")
                .userInfo(AuthResponse.UserInfo.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .isEmailVerified(user.getIsEmailVerified())
                        .build())
                .build();
    }

    public AuthResponse refreshToken(String refreshToken) {
        log.info("Attempting to refresh token");

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new AuthenticationException("Invalid refresh token");
        }

        String email = jwtUtil.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        if (!user.getIsActive()) {
            throw new AuthenticationException("Account is deactivated");
        }

        String newToken = jwtUtil.generateToken(user.getId(),user.getEmail(), user.getRole().name());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getId(),user.getEmail(),user.getRole().name());

        return AuthResponse.builder()
                .token(newToken)
                .refreshToken(newRefreshToken)
                .expiresIn(jwtUtil.getExpirationTime())
                .message("Token refreshed successfully")
                .build();
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        log.info("Password reset requested for email: {}", request.getEmail());

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (!userOpt.isPresent()) {
            // Don't reveal whether email exists
            log.warn("Password reset requested for non-existent email: {}", request.getEmail());
            return;
        }

        User user = userOpt.get();
        user.setResetPasswordToken(UUID.randomUUID().toString());
        user.setResetPasswordExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        // TODO: Send email with reset link
        log.info("Password reset token generated for user: {}", user.getEmail());
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.to=user.getEmail();
        emailRequest.subject="Password reset";
        emailRequest.body="http://localhost:8080/api/auth/reset-password and the token is "+user.getResetPasswordToken();
        emailService.sendEmail(emailRequest);
    }

    public void resetPassword(ResetPasswordRequest request) {
        log.info("Attempting password reset with token");

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("Passwords do not match");
        }

        User user = userRepository.findByResetPasswordToken(request.getToken())
                .orElseThrow(() -> new AuthenticationException("Invalid reset token"));

        if (user.getResetPasswordExpiry().isBefore(LocalDateTime.now())) {
            throw new AuthenticationException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpiry(null);
        user.setFailedLoginAttempts(0);
        user.setAccountLockedUntil(null);
        userRepository.save(user);

        log.info("Password reset successful for user: {}", user.getEmail());
    }

    public void changePassword(String userEmail, ChangePasswordRequest request) {
        log.info("Attempting password change for user: {}", userEmail);

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("Passwords do not match");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new AuthenticationException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AuthenticationException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        log.info("Password changed successfully for user: {}", userEmail);
    }

    public void verifyEmail(String token) {
        log.info("Attempting email verification with token");

        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new AuthenticationException("Invalid verification token"));

        if (user.getEmailVerificationExpiry().isBefore(LocalDateTime.now())) {
            throw new AuthenticationException("Verification token has expired");
        }

        user.setIsEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationExpiry(null);
        userRepository.save(user);

        log.info("Email verified successfully for user: {}", user.getEmail());
    }

    public void logout(String token) {
        log.info("User logout requested");
        // TODO: Implement token blacklist
        log.info("User logged out successfully");
    }

    private void handleFailedLogin(User user) {
        user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);

        if (user.getFailedLoginAttempts() >= maxFailedAttempts) {
            user.setAccountLockedUntil(LocalDateTime.now().plusMinutes(accountLockDurationMinutes));
            log.warn("Account locked for user: {} due to {} failed attempts",
                    user.getEmail(), user.getFailedLoginAttempts());
        }

        userRepository.save(user);
    }
}

