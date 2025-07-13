package com.jobportal.JobPortal.repository;

import com.jobportal.JobPortal.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost, Long> {

    List<JobPost> findByRecruiterEmail(String email);

    List<JobPost> findByTitleContainingIgnoreCase(String title);

    List<JobPost> findByEmploymentTypeIgnoreCase(String employmentType);

    List<JobPost> findByRecruiterCompanyNameContainingIgnoreCase(String companyName);

    // Additional useful queries
    List<JobPost> findByIsActiveTrue();

    List<JobPost> findByIsActiveTrueOrderByCreatedAtDesc();

    List<JobPost> findByRecruiterIdAndIsActiveTrue(Long recruiterId);

    Optional<JobPost> findByIdAndIsActiveTrue(Long id);

    List<JobPost> findByLocationContainingIgnoreCase(String location);

    List<JobPost> findByMinExperienceLessThanEqualAndMaxExperienceGreaterThanEqual(
            Integer maxExp, Integer minExp);

    List<JobPost> findByMinSalaryLessThanEqualAndMaxSalaryGreaterThanEqual(
            Double maxSalary, Double minSalary);

    @Query("SELECT DISTINCT j.location FROM JobPost j WHERE j.isActive = true")
    List<String> findDistinctActiveLocations();

    @Query("SELECT DISTINCT j.employmentType FROM JobPost j WHERE j.isActive = true")
    List<String> findDistinctActiveEmploymentTypes();

    @Query("SELECT j FROM JobPost j WHERE j.isActive = true AND " +
            "(:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:employmentType IS NULL OR LOWER(j.employmentType) = LOWER(:employmentType)) AND " +
            "(:minExperience IS NULL OR j.maxExperience >= :minExperience) AND " +
            "(:maxExperience IS NULL OR j.minExperience <= :maxExperience) AND " +
            "(:minSalary IS NULL OR j.maxSalary >= :minSalary) AND " +
            "(:maxSalary IS NULL OR j.minSalary <= :maxSalary)")
    List<JobPost> searchJobs(@Param("title") String title,
                             @Param("location") String location,
                             @Param("employmentType") String employmentType,
                             @Param("minExperience") Integer minExperience,
                             @Param("maxExperience") Integer maxExperience,
                             @Param("minSalary") Double minSalary,
                             @Param("maxSalary") Double maxSalary);

    @Query("SELECT COUNT(j) FROM JobPost j WHERE j.recruiter.id = :recruiterId AND j.isActive = true")
    Long countActiveJobsByRecruiterId(@Param("recruiterId") Long recruiterId);

    @Query("SELECT j FROM JobPost j WHERE j.isActive = true AND " +
            "j.createdAt >= CURRENT_DATE - 7 ORDER BY j.createdAt DESC")
    List<JobPost> findRecentActiveJobs();

    @Query("SELECT j FROM JobPost j WHERE j.recruiter.id = :recruiterId ORDER BY j.createdAt DESC")
    List<JobPost> findByRecruiterIdOrderByCreatedAtDesc(@Param("recruiterId") Long recruiterId);

    boolean existsByRecruiterIdAndTitleIgnoreCaseAndIsActiveTrue(Long recruiterId, String title);

    @Query("SELECT j FROM JobPost j JOIN j.applicants a WHERE a.id = :studentId")
    List<JobPost> findJobsAppliedByStudent(@Param("studentId") Long studentId);

    @Query("SELECT COUNT(a) FROM JobPost j JOIN j.applicants a WHERE j.id = :jobId")
    Long countApplicantsByJobId(@Param("jobId") Long jobId);
}