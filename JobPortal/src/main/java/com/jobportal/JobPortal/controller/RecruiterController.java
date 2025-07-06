package com.jobportal.JobPortal.controller;


import com.jobportal.JobPortal.DTO.RecruiterDTO;
import com.jobportal.JobPortal.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    @PostMapping("/register")
    public ResponseEntity<RecruiterDTO> register(@RequestBody RecruiterDTO recruiterDTO) {
        return ResponseEntity.ok(recruiterService.createRecruiter(recruiterDTO));
    }
    @GetMapping("/{email}")
    public ResponseEntity<RecruiterDTO> getRecruiterByEmail(@PathVariable String email) {
        return ResponseEntity.ok(recruiterService.getRecruiterByEmail(email));
    }
    @GetMapping("/{id}")
    public ResponseEntity<RecruiterDTO> getRecruiterById(@PathVariable Long id) {
        return ResponseEntity.ok(recruiterService.getRecruiterById(id));
    }
}
