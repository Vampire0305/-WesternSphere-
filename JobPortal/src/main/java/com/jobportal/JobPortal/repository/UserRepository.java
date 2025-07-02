package com.jobportal.JobPortal.repository;

import com.jobportal.JobPortal.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,String > {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
