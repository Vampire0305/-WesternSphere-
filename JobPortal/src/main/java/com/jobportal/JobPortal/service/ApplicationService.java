package com.jobportal.JobPortal.service;

import com.jobportal.JobPortal.DTO.ApplicationDTO;
import com.jobportal.JobPortal.Enum.Status;
import com.jobportal.JobPortal.entity.Application;
import com.jobportal.JobPortal.entity.JobPost;
import com.jobportal.JobPortal.entity.Student;
import com.jobportal.JobPortal.repository.ApplicationRepository;
import com.jobportal.JobPortal.repository.JobPostRepository;
import com.jobportal.JobPortal.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JobPostRepository jobPostRepository;

    public ApplicationDTO apply(ApplicationDTO applicationDTO) {
        // Validate student exists
        Student student = studentRepository.findById(applicationDTO.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with ID: " + applicationDTO.getStudentId()));

        // Validate job post exists
        JobPost jobPost = jobPostRepository.findById(applicationDTO.getJobPostId())
                .orElseThrow(() -> new IllegalArgumentException("Job Post not found with ID: " + applicationDTO.getJobPostId()));

        // Check if application already exists
        if (applicationRepository.existsByStudentIdAndJobPostId(applicationDTO.getStudentId(), applicationDTO.getJobPostId())) {
            throw new IllegalArgumentException("Application already exists for this student and job post");
        }

        // Create new application
        Application application = Application.builder()
                .student(student)
                .jobPost(jobPost)
                .resumeUrl(applicationDTO.getResumeUrl())
                .status(applicationDTO.getStatus() != null ? applicationDTO.getStatus() : Status.PENDING)
                .coverLetter(applicationDTO.getCoverLetter())
                .isArchived(false)
                .build();

        Application savedApplication = applicationRepository.save(application);
        return mapToDTO(savedApplication);
    }

    public List<ApplicationDTO> getApplicationsByStudentId(Long studentId, Status status, Boolean includeArchived) {
        if (!studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }

        List<Application> applications;
        if (status != null && !includeArchived) {
            applications = applicationRepository.findByStudentIdAndStatusAndIsArchived(studentId, status, false);
        } else if (status != null && includeArchived) {
            applications = applicationRepository.findByStudentIdAndStatus(studentId, status);
        } else if (!includeArchived) {
            applications = applicationRepository.findByStudentIdAndIsArchived(studentId, false);
        } else {
            applications = applicationRepository.findByStudentId(studentId);
        }

        return applications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<ApplicationDTO> getApplicationsByJobId(Long jobId, Status status) {
        if (!jobPostRepository.existsById(jobId)) {
            throw new IllegalArgumentException("Job Post not found with ID: " + jobId);
        }

        List<Application> applications;
        if (status != null) {
            applications = applicationRepository.findByJobPostIdAndStatus(jobId, status);
        } else {
            applications = applicationRepository.findByJobPostId(jobId);
        }

        return applications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Page<ApplicationDTO> getAllApplicationsPaginated(Pageable pageable, Status status, Long studentId, Long jobId) {
        Page<Application> applications;

        if (status != null && studentId != null && jobId != null) {
            applications = applicationRepository.findByStatusAndStudentIdAndJobPostId(status, studentId, jobId, pageable);
        } else if (status != null && studentId != null) {
            applications = applicationRepository.findByStatusAndStudentId(status, studentId, pageable);
        } else if (status != null && jobId != null) {
            applications = applicationRepository.findByStatusAndJobPostId(status, jobId, pageable);
        } else if (status != null) {
            applications = applicationRepository.findByStatus(status, pageable);
        } else if (studentId != null) {
            applications = applicationRepository.findByStudentId(studentId, pageable);
        } else if (jobId != null) {
            applications = applicationRepository.findByJobPostId(jobId, pageable);
        } else {
            applications = applicationRepository.findAll(pageable);
        }

        return applications.map(this::mapToDTO);
    }

    public ApplicationDTO getApplicationById(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + id));
        return mapToDTO(application);
    }

    public ApplicationDTO updateStatus(Long id, Status status, String feedback) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + id));

        if (application.getStatus() == Status.WITHDRAWN) {
            throw new IllegalStateException("Cannot update status of withdrawn application");
        }

        application.setStatus(status);
        if (feedback != null && !feedback.trim().isEmpty()) {
            // Assuming you have a feedback field in your entity
            // application.setFeedback(feedback);
        }

        Application savedApplication = applicationRepository.save(application);
        return mapToDTO(savedApplication);
    }

    public ApplicationDTO archiveApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + id));

        application.setIsArchived(true);
        Application savedApplication = applicationRepository.save(application);
        return mapToDTO(savedApplication);
    }

    public ApplicationDTO unarchiveApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + id));

        application.setIsArchived(false);
        Application savedApplication = applicationRepository.save(application);
        return mapToDTO(savedApplication);
    }

    public void deleteApplication(Long id) {
        if (!applicationRepository.existsById(id)) {
            throw new IllegalArgumentException("Application not found with ID: " + id);
        }
        applicationRepository.deleteById(id);
    }

    public Map<String, Object> getStudentApplicationStatistics(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }

        Map<String, Object> statistics = new HashMap<>();

        long totalApplications = applicationRepository.countByStudentId(studentId);
        long pendingApplications = applicationRepository.countByStudentIdAndStatus(studentId, Status.PENDING);
        long acceptedApplications = applicationRepository.countByStudentIdAndStatus(studentId, Status.ACCEPTED);
        long rejectedApplications = applicationRepository.countByStudentIdAndStatus(studentId, Status.REJECTED);
        long withdrawnApplications = applicationRepository.countByStudentIdAndStatus(studentId, Status.WITHDRAWN);
        long shortlistedApplications = applicationRepository.countByStudentIdAndStatus(studentId, Status.SHORTLISTED);
        statistics.put("shortlistedApplications", shortlistedApplications);
        statistics.put("totalApplications", totalApplications);
        statistics.put("pendingApplications", pendingApplications);
        statistics.put("acceptedApplications", acceptedApplications);
        statistics.put("rejectedApplications", rejectedApplications);
        statistics.put("withdrawnApplications", withdrawnApplications);
        statistics.put("successRate", totalApplications > 0 ? (double) acceptedApplications / totalApplications * 100 : 0);

        return statistics;
    }

    public Map<String, Object> getJobApplicationStatistics(Long jobId) {
        if (!jobPostRepository.existsById(jobId)) {
            throw new IllegalArgumentException("Job Post not found with ID: " + jobId);
        }

        Map<String, Object> statistics = new HashMap<>();

        long totalApplications = applicationRepository.countByJobPostId(jobId);
        long pendingApplications = applicationRepository.countByJobPostIdAndStatus(jobId, Status.PENDING);
        long acceptedApplications = applicationRepository.countByJobPostIdAndStatus(jobId, Status.ACCEPTED);
        long rejectedApplications = applicationRepository.countByJobPostIdAndStatus(jobId, Status.REJECTED);
        long withdrawnApplications = applicationRepository.countByJobPostIdAndStatus(jobId, Status.WITHDRAWN);
        long shortlistedApplications = applicationRepository.countByJobPostIdAndStatus(jobId, Status.SHORTLISTED);
        statistics.put("shortlistedApplications", shortlistedApplications);
        statistics.put("totalApplications", totalApplications);
        statistics.put("pendingApplications", pendingApplications);
        statistics.put("acceptedApplications", acceptedApplications);
        statistics.put("rejectedApplications", rejectedApplications);
        statistics.put("withdrawnApplications", withdrawnApplications);

        return statistics;
    }

    public List<ApplicationDTO> getRecentApplications(int limit) {
        List<Application> recentApplications = applicationRepository.findTopByOrderByAppliedDateDesc(limit);
        return recentApplications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ApplicationDTO withdrawApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + id));

        if (application.getStatus() == Status.ACCEPTED || application.getStatus() == Status.REJECTED) {
            throw new IllegalStateException("Cannot withdraw application that has been processed");
        }

        application.setStatus(Status.WITHDRAWN);
        Application savedApplication = applicationRepository.save(application);
        return mapToDTO(savedApplication);
    }

    private ApplicationDTO mapToDTO(Application application) {
        return ApplicationDTO.builder()
                .id(application.getId())
                .studentId(application.getStudent().getId())
                .jobPostId(application.getJobPost().getId())
                .studentName(application.getStudent().getName()) // Assuming Student has getName() method
                .studentEmail(application.getStudent().getEmail()) // Assuming Student has getEmail() method
                .jobTitle(application.getJobPost().getTitle()) // Assuming JobPost has getTitle() method
                .companyName(application.getJobPost().getRecruiter().getCompanyName()) // Assuming JobPost has getCompanyName() method
                .resumeUrl(application.getResumeUrl())
                .status(application.getStatus())
                .appliedDate(application.getAppliedDate())
                .updatedAt(application.getUpdatedAt())
                .coverLetter(application.getCoverLetter())
                .isArchived(application.getIsArchived())
                .build();
    }
    public ApplicationDTO shortlistApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with ID: " + id));

        if (application.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Only pending applications can be shortlisted.");
        }

        application.setStatus(Status.SHORTLISTED);
        Application saved = applicationRepository.save(application);
        return mapToDTO(saved);
    }
    public List<ApplicationDTO> getShortlistedApplicationsByJobId(Long jobId) {
        List<Application> applications = applicationRepository.findByJobPostIdAndStatus(jobId, Status.SHORTLISTED);
        return applications.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

}