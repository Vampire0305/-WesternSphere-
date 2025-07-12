package com.jobportal.JobPortal.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phone;

    @NotBlank(message = "Qualification is required")
    @Size(min = 2, max = 200, message = "Qualification must be between 2 and 200 characters")
    private String qualification;

    private String resumeURL;

    private String profilePictureURL;

    private LocalDateTime dateOfBirth;

    private String currentLocation;

    private String preferredJobLocation;

    @Min(value = 0, message = "Experience years cannot be negative")
    @Max(value = 50, message = "Experience years cannot exceed 50")
    private Integer experienceYears;

    private Set<String> skills;

    private String linkedInProfile;

    private String githubProfile;

    private String portfolioURL;

    @DecimalMin(value = "0.0", inclusive = false, message = "Expected salary must be positive")
    private Double expectedSalary;

    private Boolean isAvailableForHire;

    private Boolean isProfileComplete;

    private LocalDateTime lastLoginAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;

    private Boolean isActive;

    // Profile completion percentage calculation
    public double getProfileCompletionPercentage() {
        int totalFields = 12;
        int completedFields = 0;

        if (name != null && !name.trim().isEmpty()) completedFields++;
        if (email != null && !email.trim().isEmpty()) completedFields++;
        if (phone != null && !phone.trim().isEmpty()) completedFields++;
        if (qualification != null && !qualification.trim().isEmpty()) completedFields++;
        if (resumeURL != null && !resumeURL.trim().isEmpty()) completedFields++;
        if (currentLocation != null && !currentLocation.trim().isEmpty()) completedFields++;
        if (bio != null && !bio.trim().isEmpty()) completedFields++;
        if (skills != null && !skills.isEmpty()) completedFields++;
        if (experienceYears != null) completedFields++;
        if (expectedSalary != null) completedFields++;
        if (linkedInProfile != null && !linkedInProfile.trim().isEmpty()) completedFields++;
        if (githubProfile != null && !githubProfile.trim().isEmpty()) completedFields++;

        return (double) completedFields / totalFields * 100;
    }

    // Nested DTOs for different use cases
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentSummaryDTO {
        private Long id;
        private String name;
        private String email;
        private String qualification;
        private Integer experienceYears;
        private Boolean isAvailableForHire;
        private Double profileCompletionPercentage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentProfileUpdateDTO {
        private String name;
        private String phone;
        private String currentLocation;
        private String preferredJobLocation;
        private Integer experienceYears;
        private Set<String> skills;
        private String linkedInProfile;
        private String githubProfile;
        private String portfolioURL;
        private Double expectedSalary;
        private String bio;
        private Boolean isAvailableForHire;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentSearchDTO {
        private String qualification;
        private String location;
        private Integer minExperience;
        private Integer maxExperience;
        private Set<String> skills;
        private Double minSalary;
        private Double maxSalary;
        private Boolean availableForHire;
    }
}