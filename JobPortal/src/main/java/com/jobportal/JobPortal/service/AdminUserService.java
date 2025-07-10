package com.jobportal.JobPortal.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobportal.JobPortal.DTO.AdminUserDTO;
import com.jobportal.JobPortal.Enum.Role;
import com.jobportal.JobPortal.entity.AdminUser;
import com.jobportal.JobPortal.repository.AdminUserRepository;


@Service
public class AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    public List<AdminUserDTO>getAllUsers(){
        return adminUserRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }


    public AdminUserDTO blockUser(Long id) {
        AdminUser user = adminUserRepository.findById(id).orElse(null);
        if(user != null) {
            user.setBlocked(true);
            adminUserRepository.save(user);
            return toDTO(user);
        }
        return null;

    }
    public AdminUserDTO unBlockUser(Long id) {
        AdminUser user = adminUserRepository.findById(id).orElse(null);
        if(user != null) {
            user.setBlocked(false);
            adminUserRepository.save(user);
            return toDTO(user);
        }
        return null;
    }

    public List<AdminUserDTO>getUserByRole(Role role){
        return adminUserRepository.findByRole(role).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public AdminUserDTO upadateStatus(Long id,boolean isActive) {
        AdminUser user = adminUserRepository.findById(id).orElse(null);
        if (user==null)throw new RuntimeException("User not found");
        user.setActive(isActive);
        adminUserRepository.save(user);
        return toDTO(user);
    }

    private AdminUserDTO toDTO(AdminUser admin) {
        return new AdminUserDTO(
                admin.getId(),
                admin.getName(),
                admin.getEmail(),
                admin.getRole(),
                admin.isActive(),
                admin.isBlocked());
    }
}
