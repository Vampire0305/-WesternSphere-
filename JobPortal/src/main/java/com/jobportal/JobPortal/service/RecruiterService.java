package com.jobportal.JobPortal.service;

import com.jobportal.JobPortal.DTO.RecruiterDTO;
import com.jobportal.JobPortal.entity.Recruiter;
import com.jobportal.JobPortal.entity.User;
import com.jobportal.JobPortal.exception.ValidationException;
import com.jobportal.JobPortal.repository.RecruiterRepository;
import com.jobportal.JobPortal.security.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RecruiterService {

    private final RecruiterRepository recruiterRepository;

    @Autowired
    private JWTUtil jwtUtil;

    public RecruiterDTO createRecruiter(RecruiterDTO dto) {
        log.info("Creating new recruiter with email: {}", dto.getEmail());

        validateRecruiterData(dto);

        if (recruiterRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ValidationException("Recruiter with this email already exists");
        }

        Recruiter recruiter = mapToEntity(dto);
        recruiter.setId(null);
        Recruiter savedRecruiter = recruiterRepository.save(recruiter);

        log.info("Recruiter created successfully with ID: {}", savedRecruiter.getId());
        return mapToDTO(savedRecruiter);
    }
    public RecruiterDTO createRecruiter(RecruiterDTO dto, User user) {
        log.info("Creating new recruiter with email: {}", dto.getEmail());

        validateRecruiterData(dto);

        if (recruiterRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ValidationException("Recruiter with this email already exists");
        }

        Recruiter recruiter = mapToEntity(dto);
        recruiter.setUser(user);
        recruiter.setId(null);
        Recruiter savedRecruiter = recruiterRepository.save(recruiter);

        log.info("Recruiter created successfully with ID: {}", savedRecruiter.getId());
        return mapToDTO(savedRecruiter);
    }

    public RecruiterDTO updateRecruiter(Long id, RecruiterDTO dto) {
        log.info("Updating recruiter with ID: {}", id);

        Recruiter existingRecruiter = recruiterRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Recruiter not found with ID: " + id));

        validateRecruiterData(dto);

        if (!existingRecruiter.getEmail().equals(dto.getEmail()) &&
                recruiterRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ValidationException("Recruiter with this email already exists");
        }

        updateRecruiterFields(existingRecruiter, dto);
        Recruiter updatedRecruiter = recruiterRepository.save(existingRecruiter);

        log.info("Recruiter updated successfully with ID: {}", updatedRecruiter.getId());
        return mapToDTO(updatedRecruiter);
    }

    @Transactional(readOnly = true)
    public RecruiterDTO getRecruiterById(Long id) {
        Recruiter recruiter = recruiterRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Recruiter not found with ID: " + id));
        return mapToDTO(recruiter);
    }

    @Transactional(readOnly = true)
    public RecruiterDTO getRecruiterByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be null or empty");
        }
        Recruiter recruiter = recruiterRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Recruiter not found with email: " + email));
        return mapToDTO(recruiter);
    }

    @Transactional(readOnly = true)
    public Page<RecruiterDTO> getAllRecruiters(int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return recruiterRepository.findAll(pageable).map(this::mapToDTO);
    }

    public void deleteRecruiter(Long id) {
        if (!recruiterRepository.existsById(id)) {
            throw new ValidationException("Recruiter not found with ID: " + id);
        }
        recruiterRepository.deleteById(id);
        log.info("Recruiter deleted successfully with ID: {}", id);
    }

    public void updateLastLogin(Long id) {
        Recruiter recruiter = recruiterRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Recruiter not found with ID: " + id));
        recruiter.setLastLoginAt(LocalDateTime.now());
        recruiterRepository.save(recruiter);
    }

    // Helper methods

    private void validateRecruiterData(RecruiterDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new ValidationException("Name is required");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }
        if (dto.getCompanyName() == null || dto.getCompanyName().trim().isEmpty()) {
            throw new ValidationException("Company name is required");
        }
    }

    private void updateRecruiterFields(Recruiter recruiter, RecruiterDTO dto) {
        recruiter.setName(dto.getName());
        recruiter.setEmail(dto.getEmail());
        recruiter.setPhone(dto.getPhone());
        recruiter.setCompanyName(dto.getCompanyName());
        recruiter.setCompanyDescription(dto.getCompanyDescription());
        recruiter.setCompanyWebsite(dto.getCompanyWebsite());
        recruiter.setLinkedinProfile(dto.getLinkedinProfile());
        recruiter.setCompanySize(dto.getCompanySize());
        recruiter.setIndustry(dto.getIndustry());
        recruiter.setCompanyLocation(dto.getCompanyLocation());
        recruiter.setCompanyFoundedYear(dto.getCompanyFoundedYear());
    }

    private Recruiter mapToEntity(RecruiterDTO dto) {
        return Recruiter.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .companyName(dto.getCompanyName())
                .companyDescription(dto.getCompanyDescription())
                .companyWebsite(dto.getCompanyWebsite())
                .linkedinProfile(dto.getLinkedinProfile())
                .companySize(dto.getCompanySize())
                .industry(dto.getIndustry())
                .companyLocation(dto.getCompanyLocation())
                .companyFoundedYear(dto.getCompanyFoundedYear())
                .isVerified(dto.getIsVerified() != null ? dto.getIsVerified() : false)
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
    }

    private RecruiterDTO mapToDTO(Recruiter recruiter) {
        return RecruiterDTO.builder()
                .id(recruiter.getId())
                .name(recruiter.getName())
                .email(recruiter.getEmail())
                .phone(recruiter.getPhone())
                .companyName(recruiter.getCompanyName())
                .companyDescription(recruiter.getCompanyDescription())
                .companyWebsite(recruiter.getCompanyWebsite())
                .linkedinProfile(recruiter.getLinkedinProfile())
                .companySize(recruiter.getCompanySize())
                .industry(recruiter.getIndustry())
                .companyLocation(recruiter.getCompanyLocation())
                .companyFoundedYear(recruiter.getCompanyFoundedYear())
                .isVerified(recruiter.getIsVerified())
                .isActive(recruiter.getIsActive())
                .createdAt(recruiter.getCreatedAt())
                .updatedAt(recruiter.getUpdatedAt())
                .build();
    }

    public boolean checkPayStat(String token) {
        String status=jwtUtil.extractPayStat(token);
        if (status=="PAID") return true;
        return false;
    }
    @Autowired
    private RestTemplate restTemplate;

    // This URL points to your BillingController in the Billing/Job microservice
    private final String BILLING_SERVICE_URL = "http://billingService/api/invoice";

    public String initiatePayment(Long recruiterId, Double amount) {
        // Headers are important for POST requests to specify content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // This DTO encapsulates the necessary data for the payment
        // You would create a proper DTO for this. For simplicity, we'll use a map.
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("recruiterId", recruiterId);
        requestBody.put("amount", amount);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                BILLING_SERVICE_URL + "/create-order",
                request,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Billing service responded with an error.");
        }
    }

}
