package com.jobportal.JobPortal.controller;

import com.jobportal.JobPortal.DTO.JobPostDTO;
import com.jobportal.JobPortal.repository.JobPostRepository;
import com.jobportal.JobPortal.service.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/api/jobPosts")
@Validated
public class JobPostController {

    @Autowired
    private JobPostService jobPostService;
    @Autowired
    private JobPostRepository jobPostRepository;

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobPostDTO> createJob(@Valid @RequestBody JobPostDTO dto) {
        JobPostDTO createdJob = jobPostService.postJob(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

    @GetMapping("/recruiter")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<JobPostDTO>> getByPostedEmail(
            @RequestParam
            @NotBlank(message = "Email cannot be blank")
            @Size(max = 255, message = "Email must not exceed 255 characters")
            String email) {

        List<JobPostDTO> jobs = jobPostService.getByPostedByEmail(email.trim().toLowerCase());
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/jobTitle")
    public ResponseEntity<List<JobPostDTO>> getByJobTitle(
            @RequestParam
            @NotBlank(message = "Job title cannot be blank")
            @Size(min = 2, max = 100, message = "Job title must be between 2 and 100 characters")
            String jobTitle) {

        List<JobPostDTO> jobs = jobPostService.getByJobTitle(jobTitle.trim());
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/jobType")
    public ResponseEntity<List<JobPostDTO>> getByJobType(
            @RequestParam
            @NotBlank(message = "Job type cannot be blank")
            @Size(max = 50, message = "Job type must not exceed 50 characters")
            String jobType) {

        List<JobPostDTO> jobs = jobPostService.getByJobType(jobType.trim());
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/companyName")
    public ResponseEntity<List<JobPostDTO>> getByCompanyName(
            @RequestParam
            @NotBlank(message = "Company name cannot be blank")
            @Size(min = 2, max = 200, message = "Company name must be between 2 and 200 characters")
            String companyName) {

        List<JobPostDTO> jobs = jobPostService.getByCompanyName(companyName.trim());
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPostDTO> getJobById(@PathVariable Long id) {
        JobPostDTO job = jobPostService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @GetMapping
    public ResponseEntity<List<JobPostDTO>> getAllActiveJobs() {
        List<JobPostDTO> jobs = jobPostService.getAllActiveJobs();
        return ResponseEntity.ok(jobs);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<JobPostDTO> updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobPostDTO dto) {
        JobPostDTO updatedJob = jobPostService.updateJob(id, dto);
        return ResponseEntity.ok(updatedJob);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateJob(@PathVariable Long id) {
        jobPostService.deactivateJob(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<Void> activateJob(@PathVariable Long id) {
        jobPostService.activateJob(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobPostService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobPostDTO>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String employmentType,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary) {

        List<JobPostDTO> jobs = jobPostService.searchJobs(
                title, location, employmentType, minExperience, maxExperience, minSalary, maxSalary);
        return ResponseEntity.ok(jobs);
    }
    @GetMapping("/internal/count")
    @PreAuthorize("hasRole('ADMIN')") // Only users with the ADMIN role can access this
    public ResponseEntity<Long> countInternal() {
        try {
            Long count = jobPostRepository.count();
            // Return a 200 OK status with the count in the body
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            // Handle any database or repository exceptions gracefully
            // Return a 500 Internal Server Error with a more informative message
            // You can also log the exception here for debugging
            System.err.println("Error while counting jobPosts: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L); // Or just return 0
        }
    }
}