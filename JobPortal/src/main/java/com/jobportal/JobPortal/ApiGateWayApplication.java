package com.jobportal.JobPortal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("apigateway")
public class ApiGateWayApplication {

    public static void main(String[] ars) {
        SpringApplication.run(ApiGateWayApplication.class, ars);
    }

}
