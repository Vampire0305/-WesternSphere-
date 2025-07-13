package com.jobportal.JobPortal.repository;

import com.jobportal.JobPortal.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    List<JobPost> findByRecruiterEmail(String email);

    List<JobPost> findByTitleContainingIgnoreCase(String title);

    List<JobPost> findByEmploymentTypeIgnoreCase(String employmentType);

    List<JobPost> findByRecruiterCompanyNameContainingIgnoreCase(String companyName);
}
