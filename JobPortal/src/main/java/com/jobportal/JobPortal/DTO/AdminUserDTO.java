package com.jobportal.JobPortal.DTO;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUserDTO {

    private Long id;

    @NotBlank(message = "Admin name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private String phone;

    private Boolean isActive;
    private Boolean isBlocked;

    private LocalDateTime lastLoginAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Nested for updates
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AdminProfileUpdateDTO {
        private String name;
        private String phone;
        private String role;
    }
}
