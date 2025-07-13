package com.jobportal.JobPortal.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostDTO {

    private Long id;
    private String title;
    private String jobDescription;
    private String location;
    private String employmentType;
    private Integer minExperience;
    private Integer maxExperience;
    private Double minSalary;
    private Double maxSalary;
    private Boolean isActive;
    private Long recruiterId;
    private String recruiterName; // optional, just for view
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<Long> applicantIds; // or List<StudentSummaryDTO> if needed
}
