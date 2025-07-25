package com.jobportal.JobPortal.DTO;


import com.jobportal.JobPortal.Enum.PaymentStatus;

import java.time.LocalDate;

public class PaidSubscriptionDTO {
    public Long id;
    public Long recruiterId;
    public Long employeeId;
    public String userEmail;
    public String currency;

    public PaidSubscriptionDTO(String currency,Long id, Long recruiterId, Long employeeId, String userEmail, Long planId,Double amount, PaymentStatus paymentStatus, String invoiceUrl, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.currency = currency;
        this.recruiterId = recruiterId;
        this.employeeId = employeeId;
        this.userEmail = userEmail;
        this.planId = planId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.invoiceUrl = invoiceUrl;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public PaidSubscriptionDTO() {
    }

    public Long planId;


    public Double amount;

    public PaymentStatus paymentStatus;
    public String invoiceUrl;
    public LocalDate startDate;
    public LocalDate endDate;
}
