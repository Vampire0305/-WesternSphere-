package com.jobportal.JobPortal.repository;

import com.jobportal.JobPortal.entity.PaidSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaidSubscriptionRepository extends JpaRepository<PaidSubscription, Long> {
    List<PaidSubscription> findByRecruiterId(Long recruiterId);
    List<PaidSubscription> findByEmployeeId(Long employeeId);
    List<PaidSubscription> findByUserEmail(String userEmail);
    Optional<PaidSubscription> findById(Long id);
}
