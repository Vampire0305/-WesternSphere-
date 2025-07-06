package com.jobportal.JobPortal.DTO;

import java.util.Date;

public class JobPostDTO {

    public Long id;
    public String jobTitle;
    public String jobType;
    public String jobLocation;
    public String jobDescription;
    public String companyName;
    public String postedByEmail;
    public Date postedDate;


    public JobPostDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPostedByEmail() {
        return postedByEmail;
    }

    public void setPostedByEmail(String postedByEmail) {
        this.postedByEmail = postedByEmail;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public JobPostDTO(Long id, String jobTitle, String jobDescription, String JobLocation, String jobType, String companyName, String postedByEmail, Date postedDSate) {
        this.id=id;
        this.jobTitle=jobTitle;
        this.jobType=jobType;
        this.jobDescription=jobDescription;
        this.jobLocation=jobLocation;
        this.companyName=companyName;
        this.postedByEmail=postedByEmail;
        this.postedDate=postedDate;
    }

}
