package com.jobportal.JobPortal.service;

import com.jobportal.JobPortal.DTO.JobPostDTO;
import com.jobportal.JobPortal.entity.JobPost;
import com.jobportal.JobPortal.entity.Recruiter;
import com.jobportal.JobPortal.entity.Student;
import com.jobportal.JobPortal.exception.TitleNotFoundException;
import com.jobportal.JobPortal.repository.JobPostRepository;
import com.jobportal.JobPortal.repository.RecruiterRepository;
import com.jobportal.JobPortal.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private StudentRepository studentRepository;

    private static final Set<String> VALID_EMPLOYMENT_TYPES = new HashSet<>(Arrays.asList(
            "Full-time", "Part-time", "Contract", "Internship", "Temporary", "Freelance"
    ));

    public JobPostDTO postJob(JobPostDTO dto) {
        System.out.println(dto);
        System.out.println(dto.recruiterId);

        validateJobPostDTO(dto);

        Recruiter recruiter = recruiterRepository.findById(dto.getRecruiterId())
                .orElseThrow(() -> new EntityNotFoundException("Recruiter not found with ID: " + dto.getRecruiterId()));

        if (!recruiter.getIsActive()) {
            throw new IllegalStateException("Cannot create job post for inactive recruiter");
        }

        // Sanitize inputs
        String sanitizedTitle = sanitizeInput(dto.getTitle());
        String sanitizedDescription = sanitizeInput(dto.getJobDescription());
        String sanitizedLocation = sanitizeInput(dto.getLocation());
        String sanitizedEmploymentType = sanitizeInput(dto.getEmploymentType());

        // Validate employment type
        if (sanitizedEmploymentType != null && !VALID_EMPLOYMENT_TYPES.contains(sanitizedEmploymentType)) {
            throw new IllegalArgumentException("Invalid employment type: " + sanitizedEmploymentType);
        }

        // Validate salary range
        if (dto.getMinSalary() != null && dto.getMaxSalary() != null && dto.getMinSalary() > dto.getMaxSalary()) {
            throw new IllegalArgumentException("Minimum salary cannot be greater than maximum salary");
        }

        // Validate experience range
        if (dto.getMinExperience() != null && dto.getMaxExperience() != null && dto.getMinExperience() > dto.getMaxExperience()) {
            throw new IllegalArgumentException("Minimum experience cannot be greater than maximum experience");
        }

        Set<Student> applicants = new HashSet<>();
        if (dto.getApplicantIds() != null && !dto.getApplicantIds().isEmpty()) {
            applicants = validateAndGetStudents(dto.getApplicantIds());
        }

        JobPost jobPost = JobPost.builder()
                .title(sanitizedTitle)
                .jobDescription(sanitizedDescription != null ? sanitizedDescription : "No description provided.")
                .location(sanitizedLocation != null ? sanitizedLocation : "Remote")
                .employmentType(sanitizedEmploymentType != null ? sanitizedEmploymentType : "Full-time")
                .minExperience(dto.getMinExperience() != null ? Math.max(0, dto.getMinExperience()) : 0)
                .maxExperience(dto.getMaxExperience() != null ? Math.max(0, dto.getMaxExperience()) : 0)
                .minSalary(dto.getMinSalary() != null ? Math.max(0.0, dto.getMinSalary()) : 0.0)
                .maxSalary(dto.getMaxSalary() != null ? Math.max(0.0, dto.getMaxSalary()) : 0.0)
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .recruiter(recruiter)
                .applicants(applicants)
                .build();

        JobPost saved = jobPostRepository.save(jobPost);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<JobPostDTO> getByPostedByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        List<JobPost> jobs = jobPostRepository.findByRecruiterEmail(email.toLowerCase());
        return jobs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<JobPostDTO> getByJobTitle(String jobTitle) {
        if (!StringUtils.hasText(jobTitle)) {
            throw new IllegalArgumentException("Job title cannot be null or empty");
        }

        List<JobPost> jobs = jobPostRepository.findByTitleContainingIgnoreCase(jobTitle.trim());
        return jobs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<JobPostDTO> getByJobType(String jobType) {
        if (!StringUtils.hasText(jobType)) {
            throw new IllegalArgumentException("Job type cannot be null or empty");
        }

        List<JobPost> jobs = jobPostRepository.findByEmploymentTypeIgnoreCase(jobType.trim());
        return jobs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<JobPostDTO> getByCompanyName(String companyName) {
        if (!StringUtils.hasText(companyName)) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }

        List<JobPost> jobs = jobPostRepository.findByRecruiterCompanyNameContainingIgnoreCase(companyName.trim());
        return jobs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public JobPostDTO getJobById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Job ID must be a positive number");
        }

        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job post not found with ID: " + id));

        return mapToDTO(jobPost);
    }

    @Transactional(readOnly = true)
    public List<JobPostDTO> getAllActiveJobs() {
        List<JobPost> jobs = jobPostRepository.findByIsActiveTrue();
        return jobs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public JobPostDTO updateJob(Long id, JobPostDTO dto) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Job ID must be a positive number");
        }

        JobPost existingJob = jobPostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job post not found with ID: " + id));

        validateJobPostDTO(dto);

        // Update fields
        if (StringUtils.hasText(dto.getTitle())) {
            existingJob.setTitle(sanitizeInput(dto.getTitle()));
        }
        if (dto.getJobDescription() != null) {
            existingJob.setJobDescription(sanitizeInput(dto.getJobDescription()));
        }
        if (dto.getLocation() != null) {
            existingJob.setLocation(sanitizeInput(dto.getLocation()));
        }
        if (dto.getEmploymentType() != null) {
            String sanitizedType = sanitizeInput(dto.getEmploymentType());
            if (!VALID_EMPLOYMENT_TYPES.contains(sanitizedType)) {
                throw new IllegalArgumentException("Invalid employment type: " + sanitizedType);
            }
            existingJob.setEmploymentType(sanitizedType);
        }
        if (dto.getMinExperience() != null) {
            existingJob.setMinExperience(Math.max(0, dto.getMinExperience()));
        }
        if (dto.getMaxExperience() != null) {
            existingJob.setMaxExperience(Math.max(0, dto.getMaxExperience()));
        }
        if (dto.getMinSalary() != null) {
            existingJob.setMinSalary(Math.max(0.0, dto.getMinSalary()));
        }
        if (dto.getMaxSalary() != null) {
            existingJob.setMaxSalary(Math.max(0.0, dto.getMaxSalary()));
        }
        if (dto.getIsActive() != null) {
            existingJob.setIsActive(dto.getIsActive());
        }

        // Validate ranges after updates
        if (existingJob.getMinSalary() != null && existingJob.getMaxSalary() != null
                && existingJob.getMinSalary() > existingJob.getMaxSalary()) {
            throw new IllegalArgumentException("Minimum salary cannot be greater than maximum salary");
        }

        if (existingJob.getMinExperience() != null && existingJob.getMaxExperience() != null
                && existingJob.getMinExperience() > existingJob.getMaxExperience()) {
            throw new IllegalArgumentException("Minimum experience cannot be greater than maximum experience");
        }

        JobPost saved = jobPostRepository.save(existingJob);
        return mapToDTO(saved);
    }

    public void deactivateJob(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Job ID must be a positive number");
        }

        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job post not found with ID: " + id));

        jobPost.setIsActive(false);
        jobPostRepository.save(jobPost);
    }

    public void activateJob(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Job ID must be a positive number");
        }

        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Job post not found with ID: " + id));

        jobPost.setIsActive(true);
        jobPostRepository.save(jobPost);
    }

    public void deleteJob(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Job ID must be a positive number");
        }

        if (!jobPostRepository.existsById(id)) {
            throw new EntityNotFoundException("Job post not found with ID: " + id);
        }

        jobPostRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<JobPostDTO> searchJobs(String title, String location, String employmentType,
                                       Integer minExperience, Integer maxExperience,
                                       Double minSalary, Double maxSalary) {

        List<JobPost> jobs = jobPostRepository.searchJobs(
                title, location, employmentType, minExperience, maxExperience, minSalary, maxSalary);

        return jobs.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private void validateJobPostDTO(JobPostDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Job post data cannot be null");
        }

        if (!StringUtils.hasText(dto.getTitle())) {
            throw new TitleNotFoundException("Job title is required and cannot be empty");
        }

        if (dto.getTitle().trim().length() > 200) {
            throw new IllegalArgumentException("Job title cannot exceed 200 characters");
        }

        if (dto.getRecruiterId() == null || dto.getRecruiterId() <= 0) {
            throw new IllegalArgumentException("Valid recruiter ID is required");
        }

        if (dto.getJobDescription() != null && dto.getJobDescription().length() > 5000) {
            throw new IllegalArgumentException("Job description cannot exceed 5000 characters");
        }

        if (dto.getLocation() != null && dto.getLocation().trim().length() > 255) {
            throw new IllegalArgumentException("Location cannot exceed 255 characters");
        }

        if (dto.getMinExperience() != null && dto.getMinExperience() < 0) {
            throw new IllegalArgumentException("Minimum experience cannot be negative");
        }

        if (dto.getMaxExperience() != null && dto.getMaxExperience() < 0) {
            throw new IllegalArgumentException("Maximum experience cannot be negative");
        }

        if (dto.getMinSalary() != null && dto.getMinSalary() < 0) {
            throw new IllegalArgumentException("Minimum salary cannot be negative");
        }

        if (dto.getMaxSalary() != null && dto.getMaxSalary() < 0) {
            throw new IllegalArgumentException("Maximum salary cannot be negative");
        }
    }

    private Set<Student> validateAndGetStudents(Set<Long> applicantIds) {
        if (applicantIds == null || applicantIds.isEmpty()) {
            return new HashSet<>();
        }

        // Remove null values and invalid IDs
        Set<Long> validIds = applicantIds.stream()
                .filter(Objects::nonNull)
                .filter(id -> id > 0)
                .collect(Collectors.toSet());

        if (validIds.isEmpty()) {
            return new HashSet<>();
        }

        List<Student> students = studentRepository.findAllById(validIds);

        // Check if all students exist and are active
        Set<Long> foundIds = students.stream()
                .filter(student -> student.getIsActive())
                .map(Student::getId)
                .collect(Collectors.toSet());

        Set<Long> notFoundIds = validIds.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toSet());

        if (!notFoundIds.isEmpty()) {
            throw new EntityNotFoundException("Students not found or inactive with IDs: " + notFoundIds);
        }

        return new HashSet<>(students);
    }

    private String sanitizeInput(String input) {
        if (input == null) return null;

        return input.trim()
                .replaceAll("\\s+", " ")  // Replace multiple spaces with single space
                .replaceAll("[<>\"'&]", ""); // Remove potentially harmful characters
    }

    private JobPostDTO mapToDTO(JobPost jobPost) {
        if (jobPost == null) return null;

        return JobPostDTO.builder()
                .id(jobPost.getId())
                .title(jobPost.getTitle())
                .jobDescription(jobPost.getJobDescription())
                .location(jobPost.getLocation())
                .employmentType(jobPost.getEmploymentType())
                .minExperience(jobPost.getMinExperience())
                .maxExperience(jobPost.getMaxExperience())
                .minSalary(jobPost.getMinSalary())
                .maxSalary(jobPost.getMaxSalary())
                .isActive(jobPost.getIsActive())
                .recruiterId(jobPost.getRecruiter() != null ? jobPost.getRecruiter().getId() : null)
                .recruiterName(jobPost.getRecruiter() != null ? jobPost.getRecruiter().getName() : null)
                .createdAt(jobPost.getCreatedAt())
                .updatedAt(jobPost.getUpdatedAt())
                .applicantIds(jobPost.getApplicants() != null ?
                        jobPost.getApplicants().stream()
                                .map(Student::getId)
                                .collect(Collectors.toSet()) :
                        new HashSet<>())
                .build();
    }
}