package com.jobportal.JobPortal.entity;

import com.jobportal.JobPortal.Enum.PaymentStatus;
import com.jobportal.JobPortal.Enum.PaymentType;
import com.jobportal.JobPortal.Enum.ServiceType;
import lombok.*;


import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name="invoice")
@Getter
@Setter
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userEmail;
    private String serviceType;



    private Double amount;
    private LocalDate purchaseDate;
    private PaymentType paymentMethod;
    private String status;
    private String invoiceNumber;
    private String invoiceDownloadUrl;
    private Long subscriptionPlanId;
    public Invoice(Long id, String userEmail, String serviceType, Double amount,  String status,PaymentType paymentMethod, String invoiceNumber, String invoiceDownloadUrl, Long subscriptionPlanId) {
        this.id = id;
        this.userEmail = userEmail;
        this.serviceType = serviceType;
        this.amount = amount;
        this.purchaseDate = LocalDate.now();
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDownloadUrl = invoiceDownloadUrl;
        this.subscriptionPlanId = subscriptionPlanId;
    }
}
