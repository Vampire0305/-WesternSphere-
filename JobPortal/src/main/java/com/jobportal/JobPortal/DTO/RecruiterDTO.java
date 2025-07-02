package com.jobportal.JobPortal.DTO;

public class RecruiterDTO {

    public Long id;
    public String name;
    public String email;
    public String phone;
    public String companyName;

    public RecruiterDTO(Long id, String name, String email, String phone, String companyName, String companyDescription, String companyWebsite) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.companyName = companyName;
        this.companyDescription = companyDescription;
        this.companyWebsite = companyWebsite;
    }

    public String companyDescription;
    public String companyWebsite;

    public RecruiterDTO() {}
}
