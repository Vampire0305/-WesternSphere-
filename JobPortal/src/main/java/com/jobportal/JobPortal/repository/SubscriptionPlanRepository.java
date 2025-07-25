package com.jobportal.JobPortal.repository;

import com.jobportal.JobPortal.entity.Recruiter;
import com.jobportal.JobPortal.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    SubscriptionPlan findByRazorpayOrderId(String razorpayOrderId);
}
