package com.jobportal.JobPortal.repository;

import com.jobportal.JobPortal.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByName(String name);
    Optional<Student> findById(long id);
    Optional<Student> findByEmail(String email);


}
