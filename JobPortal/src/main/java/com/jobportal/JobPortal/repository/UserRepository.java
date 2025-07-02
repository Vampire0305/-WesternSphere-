package com.jobportal.JobPortal.repository;

import com.jobportal.JobPortal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long > {
    Optional<User> findByName(String Name);
    Optional<User> findByEmail(String email);
}
