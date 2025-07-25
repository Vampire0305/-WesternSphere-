package com.jobportal.JobPortal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RazorPayApplication {

    public static void main(String[] ars) {
        SpringApplication.run(RazorPayApplication.class, ars);
    }

}
