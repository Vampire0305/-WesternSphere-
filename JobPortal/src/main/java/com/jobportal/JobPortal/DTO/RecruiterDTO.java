package com.jobportal.JobPortal.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jobportal.JobPortal.entity.Recruiter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecruiterDTO {

    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
    private String phone;

    @NotBlank
    @Size(min = 2, max = 200)
    private String companyName;

    @Size(max = 2000)
    private String companyDescription;

    @Pattern(regexp = "^(https?://)?(www\\.)?[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}.*$")
    private String companyWebsite;

    private String linkedinProfile;

    private Recruiter.CompanySize companySize;

    private String industry;
    private String companyLocation;

    @Min(1800)
    @Max(2024)
    private Integer companyFoundedYear;

    private Boolean isVerified;
    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
