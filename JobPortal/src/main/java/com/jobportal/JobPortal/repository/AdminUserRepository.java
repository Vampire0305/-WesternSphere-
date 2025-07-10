package com.jobportal.JobPortal.repository;

import java.util.List;

import com.jobportal.JobPortal.Enum.Role;
import com.jobportal.JobPortal.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser,Long>{
    AdminUser findByEmail(String email);
    List<AdminUser> findByRole(Role role);

}
