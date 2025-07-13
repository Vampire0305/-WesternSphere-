package com.jobportal.JobPortal.repository;

import com.jobportal.JobPortal.Enum.Role;
import com.jobportal.JobPortal.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    Optional<AdminUser> findByEmail(String email);

    List<AdminUser> findByRole(Role role);

    List<AdminUser> findByIsActive(boolean isActive);

    List<AdminUser> findByIsBlocked(boolean isBlocked);
}
