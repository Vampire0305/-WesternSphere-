package com.jobportal.JobPortal.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.jobportal.JobPortal.DTO.JobPostDTO;
import com.jobportal.JobPortal.DTO.RecruiterDTO;
import com.jobportal.JobPortal.repository.RecruiterRepository;
import com.jobportal.JobPortal.service.JobPostService; // Assuming this is still needed for some direct calls or context
import com.jobportal.JobPortal.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    @Autowired
    private RestTemplate restTemplate; // Assuming you have RestTemplate configured for Eureka

    // Corrected to 'jobService' (lowercase) based on your latest config
    private final String JOB_POST_SERVICE_URL = "http://jobService/api/jobPosts";
    private final String PAYMENT_SERVICE_URL = "http://paymentService/api/recruiters";
    @Autowired
    private RecruiterRepository recruiterRepository;

    @PostMapping("/register")
    public ResponseEntity<RecruiterDTO> register(@RequestBody RecruiterDTO recruiterDTO) {
        return ResponseEntity.ok(recruiterService.createRecruiter(recruiterDTO));
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<RecruiterDTO> getRecruiterByEmail(@PathVariable String email) {
        return ResponseEntity.ok(recruiterService.getRecruiterByEmail(email));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<RecruiterDTO> getRecruiterById(@PathVariable Long id) {
        return ResponseEntity.ok(recruiterService.getRecruiterById(id));
    }

    // New endpoints to call JobPostController
    @PostMapping("/jobs")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobPostDTO> createJob(@RequestHeader("Authorization") String token, @RequestBody JobPostDTO dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<JobPostDTO> requestEntity = new HttpEntity<>(dto, headers);
        // Forward the request to the JobPostController in the Job-Post-Service
        return restTemplate.exchange(JOB_POST_SERVICE_URL, HttpMethod.POST, requestEntity, JobPostDTO.class);
    }

    @PutMapping("/jobs/{id}")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<Void> updateJob(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody JobPostDTO dto) {

        boolean isPaid=recruiterService.checkPayStat(token);
        if(isPaid) {
            // 1. Create a HttpHeaders object and set the Authorization header
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            // 2. Create an HttpEntity with the DTO and headers
            HttpEntity<JobPostDTO> requestEntity = new HttpEntity<>(dto, headers);

            // 3. Use RestTemplate's exchange method to send the request
            //    and handle the response
            restTemplate.exchange(
                    JOB_POST_SERVICE_URL + "/" + id,
                    HttpMethod.PUT,
                    requestEntity,
                    Void.class // Expecting no content in return
            );

            // The request was successful, return a 204 No Content response
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/jobs/{id}")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteJob(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers); // No body for DELETE

        restTemplate.exchange(
                JOB_POST_SERVICE_URL + "/" + id,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );
        return ResponseEntity.noContent().build();
    }

    private final String BILLING_SERVICE_URL = "http://billingService/api/invoice";

    @PostMapping("/initiate-payment/{recruiterId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<String> initiatePayment(@PathVariable Long recruiterId,@RequestBody JsonNode payload) {
        // Prepare the request to be sent to the BillingService
        Double amount = payload.get("amount").asDouble();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("recruiterId", recruiterId);
        requestBody.put("amount", amount);

        // This is a simple DTO to send the recruiter ID and amount to the billing service
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            // Make the HTTP POST call to the BillingController's 'create-order' endpoint
            ResponseEntity<String> response = restTemplate.postForEntity(
                    BILLING_SERVICE_URL + "/create-order",
                    request,
                    String.class
            );

            // Forward the response (which contains the Razorpay order ID) back to the client
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            // Handle any exceptions that occur during the process
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to initiate payment: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/pay")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> pay(@RequestBody String payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        // The HttpEntity body is the raw payload string, not a Map
        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        try {
            // Make the HTTP POST call to the BillingController's 'create-order' endpoint
            // The return type is String.class, not byte[].class
            ResponseEntity<ByteArrayInputStream> response = restTemplate.postForEntity(
                    BILLING_SERVICE_URL + "/razorpay/webhook",
                    request,
                    ByteArrayInputStream.class
            );

            // Return the body from the webhook's response
            return ResponseEntity.ok(response.getBody());
        }catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An internal error occurred while processing the request.");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse.toString());
        }
    }
    @GetMapping("/internal/count")
    @PreAuthorize("hasRole('ADMIN')") // Only users with the ADMIN role can access this
    public ResponseEntity<Long> countInternal() {
        try {
            Long count = recruiterRepository.count();
            // Return a 200 OK status with the count in the body
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            // Handle any database or repository exceptions gracefully
            // Return a 500 Internal Server Error with a more informative message
            // You can also log the exception here for debugging
            System.err.println("Error while counting recruiters: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L); // Or just return 0
        }
    }


}