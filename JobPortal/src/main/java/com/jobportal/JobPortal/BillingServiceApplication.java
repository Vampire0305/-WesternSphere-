package com.jobportal.JobPortal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableDiscoveryClient
public class BillingServiceApplication {

    public static void main(String[] ars) {
        SpringApplication.run(BillingServiceApplication.class, ars);
    }

}
