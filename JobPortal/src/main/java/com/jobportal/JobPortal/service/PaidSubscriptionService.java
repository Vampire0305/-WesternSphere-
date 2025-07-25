package com.jobportal.JobPortal.service;

import com.jobportal.JobPortal.DTO.PaidSubscriptionDTO;
import com.jobportal.JobPortal.entity.PaidSubscription;
import com.jobportal.JobPortal.repository.PaidSubscriptionRepository;
import com.jobportal.JobPortal.security.InvoiceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaidSubscriptionService {

    @Autowired
    private PaidSubscriptionRepository paidSubscriptionRepository;

    public PaidSubscriptionDTO createPaidSubscription(PaidSubscriptionDTO dto) {
        PaidSubscription paidSubscription = new PaidSubscription();
        paidSubscription.setId(dto.id);
        paidSubscription.setEmployeeId(dto.employeeId);
        paidSubscription.setRecruiterId(dto.recruiterId);
        paidSubscription.setUserEmail(dto.userEmail);
        paidSubscription.setPlanId(dto.planId);
        paidSubscription.setPaymentStatus(dto.paymentStatus);
        paidSubscription.setStartDate(dto.startDate);
        paidSubscription.setEndDate(dto.endDate);
        paidSubscription.setInvoiceUrl(dto.invoiceUrl);
        paidSubscription.setAmount(dto.amount);
        paidSubscription.setCurrency(dto.currency);
        PaidSubscription saved = paidSubscriptionRepository.save(paidSubscription);
        return mapToDTO(saved);
    }

    private PaidSubscriptionDTO mapToDTO(PaidSubscription saved) {
        PaidSubscriptionDTO dto = new PaidSubscriptionDTO();
        dto.id = saved.getId();
        dto.employeeId = saved.getEmployeeId();
        dto.recruiterId = saved.getRecruiterId();
        dto.userEmail = saved.getUserEmail();
        dto.planId = saved.getPlanId();
        dto.amount = saved.getAmount();
        dto.paymentStatus = saved.getPaymentStatus();
        dto.startDate = saved.getStartDate();
        dto.endDate = saved.getEndDate();
        dto.invoiceUrl = saved.getInvoiceUrl();
        dto.currency = saved.getCurrency();
        return dto;

    }
    public List<PaidSubscriptionDTO> getSubscriptionByUserEmail(String userEmail) {
        return paidSubscriptionRepository.findByUserEmail(userEmail).stream().map(this::mapToDTO).collect(Collectors.toList());

    }
    public List<PaidSubscriptionDTO> getSubscriptionByRecruiterId(Long recruiterId) {
        return paidSubscriptionRepository.findByRecruiterId(recruiterId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public List<PaidSubscriptionDTO> getSubscriptionByEmployeeId(Long employeeId) {
        return paidSubscriptionRepository.findByEmployeeId(employeeId).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public List<PaidSubscriptionDTO> getAllSubscriptions() {
        return paidSubscriptionRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }
//    public ByteArrayInputStream generateInvoice(Long id){
//        PaidSubscription saved = paidSubscriptionRepository.findById(id).orElse(null);
//        return InvoiceGenerator.generateInvoice(saved);
//    }

}
