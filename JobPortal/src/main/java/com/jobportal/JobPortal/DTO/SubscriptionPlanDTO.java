package com.jobportal.JobPortal.DTO;

public class SubscriptionPlanDTO {


    public Long id;
    public String name;
    public Double price;
    public String description;
    public Integer durationInDays;
    public String razorpayOrderId;
    public String razorpayPaymentId;


    public SubscriptionPlanDTO() {}

    public SubscriptionPlanDTO(Long id, String name, Double price, String description, Integer durationInDays, String razorpayOrderId, String razorpayPaymentId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.durationInDays = durationInDays;
        this.razorpayOrderId = razorpayOrderId;
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(Integer durationInDays) {
        this.durationInDays = durationInDays;
    }
}
