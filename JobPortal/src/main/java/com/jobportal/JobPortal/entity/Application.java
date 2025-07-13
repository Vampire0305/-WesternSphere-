package com.jobportal.JobPortal.entity;

import com.jobportal.JobPortal.Enum.Status;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "applications", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "job_post_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Student applying to the job
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // Job being applied to
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_post_id", nullable = false)
    private JobPost jobPost;

    // Resume URL submitted during application
    @Column(name = "resume_url", nullable = true)
    private String resumeUrl;

    // Application status (e.g., PENDING, ACCEPTED, REJECTED)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    // Timestamp when the application was made
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, name = "applied_date")
    private Date appliedDate;

    // Timestamp when the application status was last updated
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    // Optional message or cover letter
    @Lob
    @Column(name = "cover_letter")
    private String coverLetter;

    // Whether this application is archived by the student
    @Builder.Default
    @Column(name = "is_archived")
    private Boolean isArchived = false;
}
