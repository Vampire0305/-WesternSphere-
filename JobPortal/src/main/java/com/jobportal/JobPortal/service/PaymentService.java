
package com.jobportal.JobPortal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jobportal.JobPortal.DTO.PaymentDTO;
import com.jobportal.JobPortal.entity.Payment;
import com.jobportal.JobPortal.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentDTO makePayment(PaymentDTO dto) {
        Payment pay = new Payment();
        pay.setId(dto.getId());
        pay.setUserId(dto.getUserId());
        pay.setPlanId(dto.getPlanId());
        pay.setAmount(dto.getAmount());
        pay.setCurrency(dto.getCurrency());
        pay.setPaymentType(dto.getPaymentType());
        pay.setPaymentStatus(dto.getPaymentStatus());
        pay.setPaymentDate(dto.getPaymentDate());
        pay.setTransactionId(dto.getTransactionId());


        Payment saved= paymentRepository.save(pay);
        dto.setId(saved.getId());
        dto.setPaymentDate(saved.getPaymentDate());
        dto.setPaymentStatus(saved.getPaymentStatus());
        return dto;
    }

    public List<PaymentDTO> getAllPayments(){
        return paymentRepository.findAll().stream().map(p ->{
                    PaymentDTO dto= new PaymentDTO();
                    dto.setId(p.getId());
                    dto.setPlanId(p.getPlanId());
                    dto.setUserId(p.getUserId());
                    dto.setAmount(p.getAmount());
                    dto.setCurrency(p.getCurrency());
                    dto.setPaymentType(p.getPaymentType());
                    dto.setPaymentStatus(p.getPaymentStatus());
                    dto.setPaymentDate(p.getPaymentDate());
                    return dto;})
                .collect(Collectors.toList());




    }

}
