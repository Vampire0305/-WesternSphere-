package com.jobportal.JobPortal.controller;

import java.util.List;

import com.jobportal.JobPortal.DTO.RecruiterDTO;
import com.jobportal.JobPortal.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.jobportal.JobPortal.DTO.AdminUserDTO;
import com.jobportal.JobPortal.Enum.Role;
import com.jobportal.JobPortal.service.AdminUserService;

@RestController
@RequestMapping("/api/admins")
public class AdminController {


    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private AdminUserRepository adminUserRepository;

    @PostMapping("/register")
    public ResponseEntity<AdminUserDTO> register(@RequestBody AdminUserDTO adminUser) {
        return ResponseEntity.ok(adminUserService.createAdmin(adminUser));
    }


    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDTO>>getAllUsers(){
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AdminUserDTO>updateUserStatus(@RequestParam Long id, @RequestParam boolean active){
        return ResponseEntity.ok(adminUserService.updateStatus(id, active));
    }
    @PutMapping("/{id}/block")
    public ResponseEntity<AdminUserDTO>blockUser(@PathVariable Long id){
        AdminUserDTO dto=adminUserService.blockUser(id);
        if(dto==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}/unBlock")
    public ResponseEntity<AdminUserDTO>unBlockUser(@PathVariable Long id){
        AdminUserDTO dto=adminUserService.blockUser(id);
        if(dto==null) {
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(dto);

    }
    @GetMapping("/internal/count")
    @PreAuthorize("hasRole('ADMIN')") // Only users with the ADMIN role can access this
    public ResponseEntity<Long> countInternal() {
        try {
            Long count = adminUserRepository.count();
            // Return a 200 OK status with the count in the body
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            // Handle any database or repository exceptions gracefully
            // Return a 500 Internal Server Error with a more informative message
            // You can also log the exception here for debugging
            System.err.println("Error while counting admins: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L); // Or just return 0
        }
    }
}
