package com.jobportal.JobPortal.DTO;

public class AnalysticsResponse {


    public Long totalUsers;
    public Long totalStudents;
    public Long totalRecruiters;
    public Long totalJobPosts;
    public Long totalApplications;
    public Long totalAdmins;
    public Long totalSubscriptionPlan;
    



    public AnalysticsResponse() {}

    public AnalysticsResponse(Long totalUsers,Long totalStudents,Long totalRecruiters, Long totalJobPosts, Long totalApplications,Long totalAdmins,Long totalSubscriptionPlan ) {
        this.totalUsers = totalUsers;
        this.totalStudents=totalStudents;
        this.totalRecruiters=totalRecruiters;
        this.totalJobPosts=totalJobPosts;
        this.totalApplications=totalApplications;
        this.totalAdmins=totalAdmins;
        this.totalSubscriptionPlan=totalSubscriptionPlan;

    }

}