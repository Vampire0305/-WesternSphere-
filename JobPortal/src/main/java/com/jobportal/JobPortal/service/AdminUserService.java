package com.jobportal.JobPortal.service;

import com.jobportal.JobPortal.DTO.AdminUserDTO;
import com.jobportal.JobPortal.Enum.Role;
import com.jobportal.JobPortal.entity.AdminUser;
import com.jobportal.JobPortal.entity.User;
import com.jobportal.JobPortal.exception.ValidationException;
import com.jobportal.JobPortal.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;

    public AdminUserDTO createAdmin(AdminUserDTO dto) {
        log.info("Creating new admin user with email: {}", dto.getEmail());

        validateAdminData(dto);

        if (adminUserRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ValidationException("Admin with this email already exists");
        }

        AdminUser admin = mapToEntity(dto);
        admin.setId(null);
        AdminUser savedAdmin = adminUserRepository.save(admin);

        log.info("Admin created successfully with ID: {}", savedAdmin.getId());
        return mapToDTO(savedAdmin);
    }
    public AdminUserDTO createAdmin(AdminUserDTO dto, User user) {
        log.info("Creating new admin user with email: {}", dto.getEmail());

        validateAdminData(dto);

        if (adminUserRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ValidationException("Admin with this email already exists");
        }

        AdminUser admin = mapToEntity(dto);
        admin.setUser(user);
        admin.setId(null);
        AdminUser savedAdmin = adminUserRepository.save(admin);

        log.info("Admin created successfully with ID: {}", savedAdmin.getId());
        return mapToDTO(savedAdmin);
    }

    public AdminUserDTO updateAdmin(Long id, AdminUserDTO dto) {
        log.info("Updating admin with ID: {}", id);

        AdminUser existingAdmin = adminUserRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Admin not found with ID: " + id));

        validateAdminData(dto);

        if (!existingAdmin.getEmail().equals(dto.getEmail()) &&
                adminUserRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ValidationException("Admin with this email already exists");
        }

        updateAdminFields(existingAdmin, dto);
        AdminUser updatedAdmin = adminUserRepository.save(existingAdmin);

        log.info("Admin updated successfully with ID: {}", updatedAdmin.getId());
        return mapToDTO(updatedAdmin);
    }

    @Transactional(readOnly = true)
    public AdminUserDTO getAdminById(Long id) {
        AdminUser admin = adminUserRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Admin not found with ID: " + id));
        return mapToDTO(admin);
    }

    @Transactional(readOnly = true)
    public AdminUserDTO getAdminByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be null or empty");
        }
        AdminUser admin = adminUserRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Admin not found with email: " + email));
        return mapToDTO(admin);
    }

    @Transactional(readOnly = true)
    public Page<AdminUserDTO> getAllAdmins(int page, int size, String sortBy, String sortDir) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return adminUserRepository.findAll(pageable).map(this::mapToDTO);
    }

    public List<AdminUserDTO> getAllUsers() {
        return adminUserRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public AdminUserDTO blockUser(Long id) {
        AdminUser user = adminUserRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Admin not found with ID: " + id));
        user.setIsBlocked(true);
        adminUserRepository.save(user);
        return mapToDTO(user);
    }

    public AdminUserDTO unBlockUser(Long id) {
        AdminUser user = adminUserRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Admin not found with ID: " + id));
        user.setIsBlocked(false);
        adminUserRepository.save(user);
        return mapToDTO(user);
    }

    public AdminUserDTO updateStatus(Long id, boolean isActive) {
        AdminUser user = adminUserRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Admin not found with ID: " + id));
        user.setIsActive(isActive);
        adminUserRepository.save(user);
        return mapToDTO(user);
    }

    public void deleteAdmin(Long id) {
        if (!adminUserRepository.existsById(id)) {
            throw new ValidationException("Admin not found with ID: " + id);
        }
        adminUserRepository.deleteById(id);
        log.info("Admin deleted successfully with ID: {}", id);
    }

    public void updateLastLogin(Long id) {
        AdminUser admin = adminUserRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Admin not found with ID: " + id));
        admin.setLastLoginAt(LocalDateTime.now());
        adminUserRepository.save(admin);
    }

    private void validateAdminData(AdminUserDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new ValidationException("Name is required");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }
    }

    private void updateAdminFields(AdminUser admin, AdminUserDTO dto) {
        admin.setName(dto.getName());
        admin.setEmail(dto.getEmail());
        admin.setPhone(dto.getPhone());
        admin.setIsActive(dto.getIsActive());
    }

    private AdminUser mapToEntity(AdminUserDTO dto) {
        return AdminUser.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .isBlocked(dto.getIsBlocked() != null ? dto.getIsBlocked() : false)
                .build();
    }

    private AdminUserDTO mapToDTO(AdminUser admin) {
        return AdminUserDTO.builder()
                .id(admin.getId())
                .name(admin.getName())
                .email(admin.getEmail())
                .phone(admin.getPhone())
                .isActive(admin.getIsActive())
                .isBlocked(admin.getIsBlocked())
                .createdAt(admin.getCreatedAt())
                .updatedAt(admin.getUpdatedAt())
                .lastLoginAt(admin.getLastLoginAt())
                .build();
    }
}
