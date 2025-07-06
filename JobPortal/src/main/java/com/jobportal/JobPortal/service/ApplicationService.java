package com.jobportal.JobPortal.service;

import com.jobportal.JobPortal.DTO.ApplicationDTO;
import com.jobportal.JobPortal.DTO.JobPostDTO;
import com.jobportal.JobPortal.Enum.Status;
import com.jobportal.JobPortal.entity.Application;
import com.jobportal.JobPortal.entity.JobPost;
import com.jobportal.JobPortal.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public ApplicationDTO apply(ApplicationDTO applicationDTO) {
        Application app = new Application();
        app.setId(applicationDTO.getId());
        app.setStudentId(applicationDTO.getStudentId());
        app.setJobId(applicationDTO.getJobId());
        app.setStatus(applicationDTO.getStatus());
        app.setAppliedDate(applicationDTO.getAppliedDate());
        app.setResumeUrl(applicationDTO.getResumeUrl());
        Application savedApp = applicationRepository.save(app);
        return mapToDTO(savedApp);

    }

    public List<ApplicationDTO> getApplicationByStudentId(Long studentId) {
        return applicationRepository.findByStudentId(studentId).stream().map(app -> new ApplicationDTO(app.getId(), app.getStudentId(), app.getJobId(),app.getStatus(),app.getAppliedDate(), app.getResumeUrl()))
                .collect(Collectors.toList());}

    public List<ApplicationDTO> getApplicationByJobId(Long jobId) {
        return applicationRepository.findByJobId(jobId).stream().map(app -> new ApplicationDTO(app.getId(), app.getStudentId(), app.getJobId(),app.getStatus(),app.getAppliedDate(), app.getResumeUrl()))
                .collect(Collectors.toList());}

    public void updateStatus(Long id, Status status) {
        Application app = applicationRepository.findById(id).orElse(null);
        app.setStatus(status);
        applicationRepository.save(app);
    }

    private ApplicationDTO mapToDTO(Application app) {
        return new ApplicationDTO(
                app.getId(),
                app.getStudentId(),
                app.getJobId(),
                app.getStatus(),
                app.getAppliedDate(),
                app.getResumeUrl()

        );
    }
}
