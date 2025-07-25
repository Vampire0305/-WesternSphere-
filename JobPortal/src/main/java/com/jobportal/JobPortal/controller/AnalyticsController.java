package com.jobportal.JobPortal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.JobPortal.DTO.AnalysticsResponse;
import com.jobportal.JobPortal.service.AnalyticsService;

@RestController
@RequestMapping("/api/analystics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/summary")
    @PreAuthorize("hasRole('ADMIN')") // Only allows users with ADMIN role
    public ResponseEntity<AnalysticsResponse> getSummary(@RequestHeader("Authorization") String token){
        // Pass the Authorization token to the service layer
        AnalysticsResponse response = analyticsService.collectData(token);
        return ResponseEntity.ok(response);
    }
}