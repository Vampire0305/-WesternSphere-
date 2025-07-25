package com.jobportal.JobPortal.entity;

import com.jobportal.JobPortal.Enum.PaymentStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="paidsubscription")
public class PaidSubscription {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Long recruiterId;
    private Long employeeId;
    private String userEmail;
    private String currency;

    public PaidSubscription(String currency,Long id, Long recruiterId, Long employeeId, String userEmail, Long planId,Double amount, PaymentStatus paymentStatus, String invoiceUrl, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.recruiterId = recruiterId;
        this.employeeId = employeeId;
        this.userEmail = userEmail;
        this.currency = currency;
        this.planId = planId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.invoiceUrl = invoiceUrl;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public PaidSubscription() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(Long recruiterId) {
        this.recruiterId = recruiterId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getInvoiceUrl() {
        return invoiceUrl;
    }

    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    private Long planId;
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    private PaymentStatus paymentStatus;
    private String invoiceUrl;
    private LocalDate startDate;
    private LocalDate endDate;
}

