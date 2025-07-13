package com.jobportal.JobPortal.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recruiters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recruiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotBlank
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String name;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    @Column(unique = true)
    private String phone;

    @NotBlank
    @Size(min = 2, max = 200)
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Size(max = 2000)
    @Column(name = "company_description", columnDefinition = "TEXT")
    private String companyDescription;

    @Pattern(regexp = "^(https?://)?(www\\.)?[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}.*$")
    private String companyWebsite;

    private String linkedinProfile;

    @Enumerated(EnumType.STRING)
    private CompanySize companySize;

    private String industry;
    private String companyLocation;

    @Min(1800)
    @Max(2024)
    private Integer companyFoundedYear;

    @Builder.Default
    private Boolean isVerified = false;

    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime lastLoginAt;

    public enum CompanySize {
        STARTUP("1-10"),
        SMALL("11-50"),
        MEDIUM("51-200"),
        LARGE("201-1000"),
        ENTERPRISE("1000+");

        private final String range;

        CompanySize(String range) {
            this.range = range;
        }

        public String getRange() {
            return range;
        }
    }
}
