package com.jobportal.JobPortal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableEurekaServer
@Profile("eureka")
public class EurekaServerApplication {
    public static void main(String[]args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }

}



