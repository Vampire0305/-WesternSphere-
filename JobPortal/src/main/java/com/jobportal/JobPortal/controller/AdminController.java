package com.jobportal.JobPortal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.JobPortal.DTO.AdminUserDTO;
import com.jobportal.JobPortal.Enum.Role;
import com.jobportal.JobPortal.service.AdminUserService;

@RestController
@RequestMapping("/api/admins")
public class AdminController {


    @Autowired
    private AdminUserService adminUserService;


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
        return ResponseEntity.ok(adminUserService.upadateStatus(id, active));
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
