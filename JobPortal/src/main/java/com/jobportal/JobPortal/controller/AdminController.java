package com.jobportal.JobPortal.controller;

import java.util.List;

import com.jobportal.JobPortal.DTO.RecruiterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jobportal.JobPortal.DTO.AdminUserDTO;
import com.jobportal.JobPortal.Enum.Role;
import com.jobportal.JobPortal.service.AdminUserService;

@RestController
@RequestMapping("/api/admins")
public class AdminController {


    @Autowired
    private AdminUserService adminUserService;

    @PostMapping("/register")
    public ResponseEntity<AdminUserDTO> register(@RequestBody AdminUserDTO adminUser) {
        return ResponseEntity.ok(adminUserService.createAdmin(adminUser));
    }


    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDTO>>getAllUsers(){
        return ResponseEntity.ok(adminUserService.getAllUsers());
    }
    @GetMapping("/role/{role}")
    public ResponseEntity<List<AdminUserDTO>>getByRole(@PathVariable Role role){
        return ResponseEntity.ok(adminUserService.getUserByRole(role));
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
}
