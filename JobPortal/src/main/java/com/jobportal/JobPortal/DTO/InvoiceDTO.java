package com.jobportal.JobPortal.DTO;

import com.jobportal.JobPortal.Enum.PaymentStatus;
import com.jobportal.JobPortal.Enum.PaymentType;
import com.jobportal.JobPortal.Enum.ServiceType;

public class InvoiceDTO {
    public Long id;
    public String userEmail;
    public String  serviceType;
    public Double amount;
    public PaymentType paymentMethod;
    public String status;
    public String invoiceNumber;
    public String invoiceDownloadUrl;
    public Long subscriptionPlanId;

    public Long getSubscriptionPlan() {
        return subscriptionPlanId;
    }

    public void setSubscriptionPlan(Long subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
    }

    public  InvoiceDTO() {}
    public InvoiceDTO(Long id, String userEmail, String  serviceType, Double amount, PaymentType paymentMethod, String status, String invoiceNumber, String invoiceDownloadUrl, Long subscriptionPlanId) {
        this.id = id;
        this.userEmail = userEmail;
        this.serviceType = serviceType;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDownloadUrl = invoiceDownloadUrl;
        this.subscriptionPlanId = subscriptionPlanId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String  serviceType) {
        this.serviceType = serviceType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public PaymentType getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentType paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceDownloadUrl() {
        return invoiceDownloadUrl;
    }

    public void setInvoiceDownloadUrl(String invoiceDownloadUrl) {
        this.invoiceDownloadUrl = invoiceDownloadUrl;
    }
}
