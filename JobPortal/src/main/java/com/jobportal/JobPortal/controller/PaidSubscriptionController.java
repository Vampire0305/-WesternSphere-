package com.jobportal.JobPortal.controller;

import com.jobportal.JobPortal.DTO.PaidSubscriptionDTO;
import com.jobportal.JobPortal.service.PaidSubscriptionService;
import com.jobportal.JobPortal.service.RazorPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("api/subscriptions")
public class PaidSubscriptionController {

    @Autowired
    private PaidSubscriptionService paidSubscriptionService;

    @PostMapping
    public ResponseEntity<PaidSubscriptionDTO> createPaidSubscription(@RequestBody PaidSubscriptionDTO paidSubscriptionDTO) {
        return ResponseEntity.ok(paidSubscriptionService.createPaidSubscription(paidSubscriptionDTO));
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<PaidSubscriptionDTO>> getPaidSubscriptionByEmail(@PathVariable String userEmail) {
        return ResponseEntity.ok(paidSubscriptionService.getSubscriptionByUserEmail(userEmail));
    }

    @GetMapping("/user/{employeeId}")
    public ResponseEntity<List<PaidSubscriptionDTO>> getPaidSubscriptionByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(paidSubscriptionService.getSubscriptionByEmployeeId(employeeId));
    }
    @GetMapping("/user/{recruiterId}")
    public ResponseEntity<List<PaidSubscriptionDTO>> getPaidSubscriptionByRecruiterId(@PathVariable Long recruiterId) {
        return ResponseEntity.ok(paidSubscriptionService.getSubscriptionByRecruiterId(recruiterId));
    }
//    @GetMapping("invoice/{id}")
//    public ResponseEntity<InputStreamSource> downloadInvoice(@PathVariable Long id) {
//        ByteArrayInputStream invoice=paidSubscriptionService.generateInvoice(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "Inline; filename=invoice.pdf");
//        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(invoice));
//
//    }
}
