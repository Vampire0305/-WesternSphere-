package com.jobportal.JobPortal.DTO;

import com.jobportal.JobPortal.Enum.Status;
import lombok.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDTO {

    private Long id;

    @NotNull(message = "Student ID cannot be null")
    private Long studentId;

    @NotNull(message = "Job Post ID cannot be null")
    private Long jobPostId;

    private String studentName;
    private String studentEmail;
    private String jobTitle;
    private String companyName;

    @Size(max = 500, message = "Resume URL cannot exceed 500 characters")
    private String resumeUrl;

    @Builder.Default
    private Status status = Status.PENDING;

    private Date appliedDate;
    private Date updatedAt;

    @Size(max = 2000, message = "Cover letter cannot exceed 2000 characters")
    private String coverLetter;

    @Builder.Default
    private Boolean isArchived = false;

    // Additional fields for enhanced functionality
    private String applicationReference;
    private Integer applicationScore;
    private String feedback;
    private String interviewNotes;
}