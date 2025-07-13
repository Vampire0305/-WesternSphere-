package com.jobportal.JobPortal.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin_users", indexes = {
        @Index(name = "idx_admin_email", columnList = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotBlank(message = "Admin name is required")
    @Column(nullable = false)
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "role")
    private String role; // e.g., SUPER_ADMIN, MODERATOR, etc.

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Boolean isBlocked = false;

    private LocalDateTime lastLoginAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }
}
