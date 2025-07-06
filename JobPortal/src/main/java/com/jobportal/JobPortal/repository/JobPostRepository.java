package com.jobportal.JobPortal.repository;

import java.util.List;

import com.jobportal.JobPortal.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface JobPostRepository extends JpaRepository<JobPost,Long>{
    List<JobPost>findByPostedByEmail(String email);
    List<JobPost>findByJobTitle(String jobTitle);
    List<JobPost>findByJobType(String jobType);
    List<JobPost>findByCompanyName(String companyName);

}
