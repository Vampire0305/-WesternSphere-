package com.jobportal.JobPortal.DTO;

import com.jobportal.JobPortal.Enum.Status;

import java.util.Date;

public class ApplicationDTO {
    public Long id;
    public Long studentId;

    public ApplicationDTO() {
    }

    public Long jobId;
    public Status status;
    public String resumeUrl;

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public ApplicationDTO(Long id, Long studentId, Long jobId, Status status, Date appliedDate, String resumeUrl) {
        this.id = id;
        this.studentId = studentId;
        this.jobId = jobId;
        this.status = status;
        this.appliedDate = appliedDate;
        this.resumeUrl = resumeUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(Date appliedDate) {
        this.appliedDate = appliedDate;
    }

    public Date appliedDate;

}
