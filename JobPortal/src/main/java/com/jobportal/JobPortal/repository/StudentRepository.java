package com.jobportal.JobPortal.repository;

import com.jobportal.JobPortal.entity.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StudentRepository extends CrudRepository<Student, Long> {

    Optional<Student> findByName(String name);
    Optional<Student> findById(long id);
    Optional<Student> findByEmail(String email);


}
