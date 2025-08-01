package com.jobportal.JobPortal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class UserPaymentStatusServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserPaymentStatusServiceApplication.class, args);
    }
}
