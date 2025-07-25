package com.jobportal.JobPortal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.jobportal.JobPortal.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long>  {



}
