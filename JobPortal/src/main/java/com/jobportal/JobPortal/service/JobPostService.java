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

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobPostService {

    @Autowired
    private JobPostRepository jobPostRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private StudentRepository studentRepository;

    public JobPostDTO postJob(JobPostDTO dto) {
        Recruiter recruiter = recruiterRepository.findById(dto.getRecruiterId())
                .orElseThrow(() -> new EntityNotFoundException("Recruiter not found"));

        if(dto.getTitle()==null || dto.getTitle().trim().isEmpty() ) {
            throw new TitleNotFoundException("Title not found");
        }

        Set<Student> applicants = new HashSet<>();
        if (dto.getApplicantIds() != null) {
            applicants = studentRepository.findAllById(dto.getApplicantIds()).stream().collect(Collectors.toSet());
        }

        JobPost jobPost = JobPost.builder()
                .title(dto.getTitle())
                .jobDescription(dto.getJobDescription() != null ? dto.getJobDescription() : "No description provided.")
                .location(dto.getLocation() != null ? dto.getLocation() : "Remote")
                .employmentType(dto.getEmploymentType() != null ? dto.getEmploymentType() : "Full-time")
                .minExperience(dto.getMinExperience() != null ? dto.getMinExperience() : 0)
                .maxExperience(dto.getMaxExperience() != null ? dto.getMaxExperience() : 0)
                .minSalary(dto.getMinSalary() != null ? dto.getMinSalary() : 0.0)
                .maxSalary(dto.getMaxSalary() != null ? dto.getMaxSalary() : 0.0)
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .recruiter(recruiter)
                .applicants(applicants)
                .build();

        JobPost saved = jobPostRepository.save(jobPost);
        return mapToDTO(saved);
    }

    public List<JobPostDTO> getByPostedByEmail(String email) {
        return jobPostRepository.findByRecruiterEmail(email)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<JobPostDTO> getByJobTitle(String jobTitle) {
        return jobPostRepository.findByTitleContainingIgnoreCase(jobTitle)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<JobPostDTO> getByJobType(String jobType) {
        return jobPostRepository.findByEmploymentTypeIgnoreCase(jobType)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<JobPostDTO> getByCompanyName(String companyName) {
        return jobPostRepository.findByRecruiterCompanyNameContainingIgnoreCase(companyName)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private JobPostDTO mapToDTO(JobPost jobPost) {
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
                .recruiterId(jobPost.getRecruiter().getId())
                .recruiterName(jobPost.getRecruiter().getName())
                .createdAt(jobPost.getCreatedAt())
                .updatedAt(jobPost.getUpdatedAt())
                .applicantIds(jobPost.getApplicants().stream()
                        .map(Student::getId)
                        .collect(Collectors.toSet()))
                .build();
    }
}
