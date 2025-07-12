package com.jobportal.JobPortal.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "students", indexes = {
        @Index(name = "idx_student_email", columnList = "email"),
        @Index(name = "idx_student_phone", columnList = "phone"),
        @Index(name = "idx_student_qualification", columnList = "qualification")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    @Column(unique = true)
    private String phone;

    @NotBlank(message = "Qualification is required")
    @Size(min = 2, max = 200, message = "Qualification must be between 2 and 200 characters")
    @Column(nullable = false)
    private String qualification;

    @Column(name = "resume_url")
    private String resumeURL;

    @Column(name = "profile_picture_url")
    private String profilePictureURL;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    @Column(name = "current_location")
    private String currentLocation;

    @Column(name = "preferred_job_location")
    private String preferredJobLocation;

    @Column(name = "experience_years")
    @Builder.Default
    private Integer experienceYears = 0;

    @ElementCollection
    @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "skill")
    private Set<String> skills;

    @Column(name = "linkedin_profile")
    private String linkedInProfile;

    @Column(name = "github_profile")
    private String githubProfile;

    @Column(name = "portfolio_url")
    private String portfolioURL;

    @Column(name = "expected_salary")
    private Double expectedSalary;

    @Column(name = "is_available_for_hire")
    @Builder.Default
    private Boolean isAvailableForHire = true;

    @Column(name = "is_profile_complete")
    @Builder.Default
    private Boolean isProfileComplete = false;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isProfileComplete = checkProfileCompleteness();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.isProfileComplete = checkProfileCompleteness();
    }

    private Boolean checkProfileCompleteness() {
        return name != null && !name.trim().isEmpty() &&
                email != null && !email.trim().isEmpty() &&
                phone != null && !phone.trim().isEmpty() &&
                qualification != null && !qualification.trim().isEmpty() &&
                resumeURL != null && !resumeURL.trim().isEmpty() &&
                currentLocation != null && !currentLocation.trim().isEmpty() &&
                bio != null && !bio.trim().isEmpty();
    }

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }
}