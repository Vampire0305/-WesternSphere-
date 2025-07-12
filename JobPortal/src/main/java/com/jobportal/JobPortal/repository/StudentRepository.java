package com.jobportal.JobPortal.repository;

import com.jobportal.JobPortal.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByName(String name);
    Optional<Student> findById(long id);
    Optional<Student> findByEmail(String email);
    Optional<Student> findByPhone(String phone);

    // For getActiveStudents
    Page<Student> findByIsActiveTrue(Pageable pageable);

    // For getAvailableStudents
    Page<Student> findByIsAvailableForHireTrue(Pageable pageable);

    // For getStudentsByQualification
    List<Student> findByQualificationContainingIgnoreCase(String qualification);

    // For getStudentsByLocation
    List<Student> findByCurrentLocationContainingIgnoreCase(String location);

    // For getStudentsBySkills
    List<Student> findBySkillsIn(Set<String> skills);

    // For getActiveStudentCount
    long countByIsActiveTrue();

    // For getAvailableStudentCount
    long countByIsAvailableForHireTrue();
}
