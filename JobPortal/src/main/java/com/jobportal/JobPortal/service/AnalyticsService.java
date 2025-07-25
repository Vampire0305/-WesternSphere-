package com.jobportal.JobPortal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.jobportal.JobPortal.DTO.AnalysticsResponse;

@Service
public class AnalyticsService {

    @Autowired
    private RestTemplate restTemplate;

    public AnalysticsResponse collectData(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Correct the URLs to include the base path for each service
        Long students = restTemplate.exchange("http://studentService/api/students/internal/count", HttpMethod.GET, entity, Long.class).getBody();
        Long recruiters = restTemplate.exchange("http://recruiterService/api/recruiters/internal/count", HttpMethod.GET, entity, Long.class).getBody();
        Long jobPosts = restTemplate.exchange("http://jobService/api/jobPosts/internal/count", HttpMethod.GET, entity, Long.class).getBody();
        Long applications = restTemplate.exchange("http://jobService/api/applications/internal/count", HttpMethod.GET, entity, Long.class).getBody();
        Long auth = restTemplate.exchange("http://authService/api/auth/internal/count", HttpMethod.GET, entity, Long.class).getBody();
        Long admins = restTemplate.exchange("http://authService/api/admins/internal/count", HttpMethod.GET, entity, Long.class).getBody();
        Long subscriptionPlan = restTemplate.exchange("http://jobService/api/subscription/internal/count", HttpMethod.GET, entity, Long.class).getBody();

        return new AnalysticsResponse(auth, students, recruiters, jobPosts, applications, admins, subscriptionPlan);
    }
}