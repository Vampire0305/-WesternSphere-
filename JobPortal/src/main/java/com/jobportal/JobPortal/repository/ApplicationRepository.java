package com.jobportal.JobPortal.repository;

import com.jobportal.JobPortal.Enum.Status;
import com.jobportal.JobPortal.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // Basic finders
    List<Application> findByStudentId(Long studentId);
    List<Application> findByJobPostId(Long jobPostId);

    // Paginated finders
    Page<Application> findByStudentId(Long studentId, Pageable pageable);
    Page<Application> findByJobPostId(Long jobPostId, Pageable pageable);
    Page<Application> findByStatus(Status status, Pageable pageable);

    // Status-based finders
    List<Application> findByStudentIdAndStatus(Long studentId, Status status);
    List<Application> findByJobPostIdAndStatus(Long jobPostId, Status status);
    List<Application> findByStudentIdAndStatusAndIsArchived(Long studentId, Status status, Boolean isArchived);

    // Archive-based finders
    List<Application> findByStudentIdAndIsArchived(Long studentId, Boolean isArchived);
    List<Application> findByJobPostIdAndIsArchived(Long jobPostId, Boolean isArchived);

    // Combined filters with pagination
    Page<Application> findByStatusAndStudentId(Status status, Long studentId, Pageable pageable);
    Page<Application> findByStatusAndJobPostId(Status status, Long jobPostId, Pageable pageable);
    Page<Application> findByStatusAndStudentIdAndJobPostId(Status status, Long studentId, Long jobPostId, Pageable pageable);

    // Existence checks
    boolean existsByStudentIdAndJobPostId(Long studentId, Long jobPostId);
    boolean existsByStudentIdAndJobPostIdAndStatus(Long studentId, Long jobPostId, Status status);

    // Count methods for statistics
    long countByStudentId(Long studentId);
    long countByJobPostId(Long jobPostId);
    long countByStudentIdAndStatus(Long studentId, Status status);
    long countByJobPostIdAndStatus(Long jobPostId, Status status);
    long countByStatus(Status status);
    long countByIsArchived(Boolean isArchived);

    // Recent applications
    @Query("SELECT a FROM Application a ORDER BY a.appliedDate DESC")
    List<Application> findTopByOrderByAppliedDateDesc(int limit);

    // Applications within date range
    @Query("SELECT a FROM Application a WHERE a.appliedDate BETWEEN :startDate AND :endDate")
    List<Application> findByAppliedDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // Find applications by student and date range
    @Query("SELECT a FROM Application a WHERE a.student.id = :studentId AND a.appliedDate BETWEEN :startDate AND :endDate")
    List<Application> findByStudentIdAndAppliedDateBetween(@Param("studentId") Long studentId,
                                                           @Param("startDate") Date startDate,
                                                           @Param("endDate") Date endDate);

    // Find applications by job post and date range
    @Query("SELECT a FROM Application a WHERE a.jobPost.id = :jobPostId AND a.appliedDate BETWEEN :startDate AND :endDate")
    List<Application> findByJobPostIdAndAppliedDateBetween(@Param("jobPostId") Long jobPostId,
                                                           @Param("startDate") Date startDate,
                                                           @Param("endDate") Date endDate);

    // Find applications by multiple statuses
    @Query("SELECT a FROM Application a WHERE a.status IN :statuses")
    List<Application> findByStatusIn(@Param("statuses") List<Status> statuses);

    // Find applications by student and multiple statuses
    @Query("SELECT a FROM Application a WHERE a.student.id = :studentId AND a.status IN :statuses")
    List<Application> findByStudentIdAndStatusIn(@Param("studentId") Long studentId, @Param("statuses") List<Status> statuses);

    // Find applications by job post and multiple statuses
    @Query("SELECT a FROM Application a WHERE a.jobPost.id = :jobPostId AND a.status IN :statuses")
    List<Application> findByJobPostIdAndStatusIn(@Param("jobPostId") Long jobPostId, @Param("statuses") List<Status> statuses);

    // Find applications with resume URL
    @Query("SELECT a FROM Application a WHERE a.resumeUrl IS NOT NULL AND a.resumeUrl != ''")
    List<Application> findApplicationsWithResume();

    // Find applications without resume URL
    @Query("SELECT a FROM Application a WHERE a.resumeUrl IS NULL OR a.resumeUrl = ''")
    List<Application> findApplicationsWithoutResume();

    // Find applications with cover letter
    @Query("SELECT a FROM Application a WHERE a.coverLetter IS NOT NULL AND a.coverLetter != ''")
    List<Application> findApplicationsWithCoverLetter();

    // Find applications by student with specific job post status
//    @Query("SELECT a FROM Application a WHERE a.student.id = :studentId AND a.jobPost.status = :jobStatus")
//    List<Application> findByStudentIdAndJobPostStatus(@Param("studentId") Long studentId, @Param("jobStatus") String jobStatus);

    // Find duplicate applications (same student, same job post)
    @Query("SELECT a FROM Application a WHERE EXISTS (SELECT a2 FROM Application a2 WHERE a2.student.id = a.student.id AND a2.jobPost.id = a.jobPost.id AND a2.id != a.id)")
    List<Application> findDuplicateApplications();

    // Find applications by company
    @Query("SELECT a FROM Application a WHERE a.jobPost.recruiter.companyName = :companyName")
    List<Application> findByCompanyName(@Param("companyName") String companyName);

    // Find applications by student and company
    @Query("SELECT a FROM Application a WHERE a.student.id = :studentId AND a.jobPost.recruiter.companyName = :companyName")
    List<Application> findByStudentIdAndCompanyName(@Param("studentId") Long studentId, @Param("companyName") String companyName);

    // Custom query for advanced filtering
    @Query("SELECT a FROM Application a WHERE " +
            "(:studentId IS NULL OR a.student.id = :studentId) AND " +
            "(:jobPostId IS NULL OR a.jobPost.id = :jobPostId) AND " +
            "(:status IS NULL OR a.status = :status) AND " +
            "(:isArchived IS NULL OR a.isArchived = :isArchived) AND " +
            "(:startDate IS NULL OR a.appliedDate >= :startDate) AND " +
            "(:endDate IS NULL OR a.appliedDate <= :endDate)")
    Page<Application> findApplicationsWithFilters(@Param("studentId") Long studentId,
                                                  @Param("jobPostId") Long jobPostId,
                                                  @Param("status") Status status,
                                                  @Param("isArchived") Boolean isArchived,
                                                  @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate,
                                                  Pageable pageable);

    // Find applications that need follow-up (pending for more than X days)
    @Query("SELECT a FROM Application a WHERE a.status = 'PENDING' AND a.appliedDate < :cutoffDate")
    List<Application> findPendingApplicationsOlderThan(@Param("cutoffDate") Date cutoffDate);

    // Find top performing students (most accepted applications)
    @Query("SELECT a.student.id, COUNT(a) as acceptedCount FROM Application a WHERE a.status = 'ACCEPTED' GROUP BY a.student.id ORDER BY acceptedCount DESC")
    List<Object[]> findTopPerformingStudents();

    // Find most popular job posts (most applications)
    @Query("SELECT a.jobPost.id, COUNT(a) as applicationCount FROM Application a GROUP BY a.jobPost.id ORDER BY applicationCount DESC")
    List<Object[]> findMostPopularJobPosts();

    // Find applications by student email
    @Query("SELECT a FROM Application a WHERE a.student.email = :email")
    List<Application> findByStudentEmail(@Param("email") String email);

    // Find applications by job title
    @Query("SELECT a FROM Application a WHERE LOWER(a.jobPost.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Application> findByJobTitleContainingIgnoreCase(@Param("title") String title);

    // Delete applications by student ID
    void deleteByStudentId(Long studentId);

    // Delete applications by job post ID
    void deleteByJobPostId(Long jobPostId);

    // Delete applications older than a certain date
    void deleteByAppliedDateBefore(Date date);
}