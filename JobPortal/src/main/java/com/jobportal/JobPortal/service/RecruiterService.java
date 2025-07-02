package com.jobportal.JobPortal.service;

import com.jobportal.JobPortal.DTO.RecruiterDTO;
import com.jobportal.JobPortal.entity.Recruiter;
import com.jobportal.JobPortal.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecruiterService {
    @Autowired
    private RecruiterRepository recruiterRepository;

    public RecruiterDTO createRecruiter(RecruiterDTO dto) {
        Recruiter recruiter = new Recruiter(dto.id,dto.name,dto.email,dto.phone,dto.companyName,dto.companyDescription,dto.companyWebsite);
        recruiter=recruiterRepository.save(recruiter);
        return dto;
    }

    public RecruiterDTO getRecruiterByEmail(String email) {
        Recruiter recruiter= recruiterRepository.findByEmail(email).get();
        if (recruiter==null) {return null;}
        return new RecruiterDTO(
                recruiter.getId(),
                recruiter.getName(),
                recruiter.getEmail(),
                recruiter.getPhone(),
                recruiter.getCompanyName(),
                recruiter.getCompanyDescription(),
                recruiter.getCompanyWebsite()
        );

    }

    public RecruiterDTO getRecruiterById(long id) {
        Recruiter recruiter=recruiterRepository.findById(id).get();
        if (recruiter==null) {return null;}
        return new RecruiterDTO(
                recruiter.getId(),
                recruiter.getName(),
                recruiter.getEmail(),
                recruiter.getPhone(),
                recruiter.getCompanyName(),
                recruiter.getCompanyDescription(),
                recruiter.getCompanyWebsite()
        );

    }

}
