package com.jobportal.JobPortal.controller;

import java.util.List;

import com.jobportal.JobPortal.repository.SubscriptionPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.JobPortal.DTO.SubscriptionPlanDTO;
import com.jobportal.JobPortal.service.SubscriptionPlanService;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionPlanController {

    @Autowired
    private SubscriptionPlanService  subscriptionPlanService;
    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;


    @GetMapping
    public ResponseEntity<List<SubscriptionPlanDTO>>getAll(){
        return ResponseEntity.ok(subscriptionPlanService.getAllSubscriptionPlan());
    }

    @PostMapping
    public ResponseEntity<SubscriptionPlanDTO> create(@RequestParam SubscriptionPlanDTO dto) {
        return ResponseEntity.ok(subscriptionPlanService.createSubscription(dto));
    }
    @GetMapping("/internal/count")
    @PreAuthorize("hasRole('ADMIN')") // Only users with the ADMIN role can access this
    public ResponseEntity<Long> countInternal() {
        try {
            Long count = subscriptionPlanRepository.count();
            // Return a 200 OK status with the count in the body
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            // Handle any database or repository exceptions gracefully
            // Return a 500 Internal Server Error with a more informative message
            // You can also log the exception here for debugging
            System.err.println("Error while counting applications: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L); // Or just return 0
        }
    }
}
