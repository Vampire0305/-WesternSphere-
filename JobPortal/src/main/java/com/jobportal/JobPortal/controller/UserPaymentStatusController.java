package com.jobportal.JobPortal.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.JobPortal.DTO.UserPaymentStatusDTO;
import com.jobportal.JobPortal.service.UserPaymentStatusService;

@RestController
@RequestMapping("/api/user_subscrptions_tatus")
public class UserPaymentStatusController {

    @Autowired
    private UserPaymentStatusService userPaymentStatusService;


    @PostMapping
    public ResponseEntity<UserPaymentStatusDTO>assign(@RequestBody UserPaymentStatusDTO dto ){
        return ResponseEntity.ok(userPaymentStatusService.assignSubscriptionPlan(dto));
    }

    @GetMapping("{userId}")
    public ResponseEntity<Optional<UserPaymentStatusDTO>> getStatus(@PathVariable Long userId){
        return ResponseEntity.ok(userPaymentStatusService.getStatusByUserId(userId));
    }
}
