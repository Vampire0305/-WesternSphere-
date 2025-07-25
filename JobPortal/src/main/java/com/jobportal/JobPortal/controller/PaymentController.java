package com.jobportal.JobPortal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.JobPortal.DTO.PaymentDTO;
import com.jobportal.JobPortal.service.PaymentService;

@RestController
@RequestMapping("/ap/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    @PostMapping
    public ResponseEntity<PaymentDTO> pay(@RequestBody PaymentDTO dto ){
        return ResponseEntity.ok(paymentService.makePayment(dto));
    }
    @GetMapping
    public ResponseEntity<List<PaymentDTO>>getAll(){
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

}
