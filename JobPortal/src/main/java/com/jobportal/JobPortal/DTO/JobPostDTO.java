package com.jobportal.JobPortal.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostDTO {

    public Long id;
    public String title;
    public String jobDescription;
    public String location;
    public String employmentType;
    public Integer minExperience;
    public Integer maxExperience;
    public Double minSalary;
    public Double maxSalary;
    public Boolean isActive;
    public Long recruiterId;
    public String recruiterName; // optional, just for view
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    public Set<Long> applicantIds; // or List<StudentSummaryDTO> if needed
}
