package com.jobportal.JobPortal.repository;

import com.jobportal.JobPortal.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Invoice findBySubscriptionPlanId(Long id);
    List<Invoice> findByUserEmail(String email);
}
