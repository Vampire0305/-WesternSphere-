package com.jobportal.JobPortal.service;

import com.jobportal.JobPortal.DTO.StudentDTO;
import com.jobportal.JobPortal.entity.Student;
import com.jobportal.JobPortal.exception.ValidationException;
import com.jobportal.JobPortal.repository.StudentRepository;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentDTO createStudent(StudentDTO dto) {
        log.info("Creating new student with email: {}", dto.getEmail());

        validateStudentData(dto);

        if (studentRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ValidationException("Student with this email already exists");
        }

        if (dto.getPhone() != null && studentRepository.findByPhone(dto.getPhone()).isPresent()) {
            throw new ValidationException("Student with this phone number already exists");
        }

        Student student = mapToEntity(dto);
        student.setId(null); // Ensure new entity

        Student savedStudent = studentRepository.save(student);
        log.info("Student created successfully with ID: {}", savedStudent.getId());

        return mapToDTO(savedStudent);
    }

    public StudentDTO updateStudent(Long id, StudentDTO dto) {
        log.info("Updating student with ID: {}", id);

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Student not found with ID: " + id));

        validateStudentData(dto);

        // Check for email conflicts
        if (!existingStudent.getEmail().equals(dto.getEmail()) &&
                studentRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ValidationException("Student with this email already exists");
        }

        // Check for phone conflicts
        if (dto.getPhone() != null && !dto.getPhone().equals(existingStudent.getPhone()) &&
                studentRepository.findByPhone(dto.getPhone()).isPresent()) {
            throw new ValidationException("Student with this phone number already exists");
        }

        updateStudentFields(existingStudent, dto);

        Student updatedStudent = studentRepository.save(existingStudent);
        log.info("Student updated successfully with ID: {}", updatedStudent.getId());

        return mapToDTO(updatedStudent);
    }

    public StudentDTO updateStudentProfile(Long id, StudentDTO.StudentProfileUpdateDTO profileDto) {
        log.info("Updating student profile with ID: {}", id);

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Student not found with ID: " + id));

        // Update only profile fields
        if (profileDto.getName() != null) existingStudent.setName(profileDto.getName());
        if (profileDto.getPhone() != null) existingStudent.setPhone(profileDto.getPhone());
        if (profileDto.getCurrentLocation() != null) existingStudent.setCurrentLocation(profileDto.getCurrentLocation());
        if (profileDto.getPreferredJobLocation() != null) existingStudent.setPreferredJobLocation(profileDto.getPreferredJobLocation());
        if (profileDto.getExperienceYears() != null) existingStudent.setExperienceYears(profileDto.getExperienceYears());
        if (profileDto.getSkills() != null) existingStudent.setSkills(profileDto.getSkills());
        if (profileDto.getLinkedInProfile() != null) existingStudent.setLinkedInProfile(profileDto.getLinkedInProfile());
        if (profileDto.getGithubProfile() != null) existingStudent.setGithubProfile(profileDto.getGithubProfile());
        if (profileDto.getPortfolioURL() != null) existingStudent.setPortfolioURL(profileDto.getPortfolioURL());
        if (profileDto.getExpectedSalary() != null) existingStudent.setExpectedSalary(profileDto.getExpectedSalary());
        if (profileDto.getBio() != null) existingStudent.setBio(profileDto.getBio());
        if (profileDto.getIsAvailableForHire() != null) existingStudent.setIsAvailableForHire(profileDto.getIsAvailableForHire());

        Student updatedStudent = studentRepository.save(existingStudent);
        log.info("Student profile updated successfully with ID: {}", updatedStudent.getId());

        return mapToDTO(updatedStudent);
    }

    @Transactional(readOnly = true)
    public StudentDTO getStudentById(Long id) {
        log.info("Fetching student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Student not found with ID: " + id));

        return mapToDTO(student);
    }

    @Transactional(readOnly = true)
    public StudentDTO getStudentByEmail(String email) {
        log.info("Fetching student with email: {}", email);

        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be null or empty");
        }

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Student not found with email: " + email));

        return mapToDTO(student);
    }

    @Transactional(readOnly = true)
    public Optional<StudentDTO> findStudentByEmail(String email) {
        log.info("Searching for student with email: {}", email);

        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }

        return studentRepository.findByEmail(email)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<StudentDTO> getAllStudents(int page, int size, String sortBy, String sortDirection) {
        log.info("Fetching all students - page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        return studentRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<StudentDTO> getActiveStudents(int page, int size) {
        log.info("Fetching active students - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        return studentRepository.findByIsActiveTrue(pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public Page<StudentDTO> getAvailableStudents(int page, int size) {
        log.info("Fetching available students - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        return studentRepository.findByIsAvailableForHireTrue(pageable)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> searchStudents(StudentDTO.StudentSearchDTO searchDto) {
        log.info("Searching students with criteria: {}", searchDto);

        // This would typically use a more sophisticated search mechanism
        // For now, implementing basic search
        return studentRepository.findAll().stream()
                .filter(student -> matchesSearchCriteria(student, searchDto))
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsByQualification(String qualification) {
        log.info("Fetching students with qualification: {}", qualification);

        if (qualification == null || qualification.trim().isEmpty()) {
            throw new ValidationException("Qualification cannot be null or empty");
        }

        return studentRepository.findByQualificationContainingIgnoreCase(qualification)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsByLocation(String location) {
        log.info("Fetching students in location: {}", location);

        if (location == null || location.trim().isEmpty()) {
            throw new ValidationException("Location cannot be null or empty");
        }

        return studentRepository.findByCurrentLocationContainingIgnoreCase(location)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> getStudentsBySkills(Set<String> skills) {
        log.info("Fetching students with skills: {}", skills);

        if (skills == null || skills.isEmpty()) {
            throw new ValidationException("Skills cannot be null or empty");
        }

        return studentRepository.findBySkillsIn(skills)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StudentDTO> getRecentlyRegisteredStudents(int limit) {
        log.info("Fetching recently registered students, limit: {}", limit);

        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        return studentRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO deactivateStudent(Long id) {
        log.info("Deactivating student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Student not found with ID: " + id));

        student.setIsActive(false);
        student.setIsAvailableForHire(false);

        Student updatedStudent = studentRepository.save(student);
        log.info("Student deactivated successfully with ID: {}", updatedStudent.getId());

        return mapToDTO(updatedStudent);
    }

    public StudentDTO activateStudent(Long id) {
        log.info("Activating student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Student not found with ID: " + id));

        student.setIsActive(true);

        Student updatedStudent = studentRepository.save(student);
        log.info("Student activated successfully with ID: {}", updatedStudent.getId());

        return mapToDTO(updatedStudent);
    }

    public void updateLastLogin(Long id) {
        log.info("Updating last login for student with ID: {}", id);

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Student not found with ID: " + id));

        student.updateLastLogin();
        studentRepository.save(student);

        log.info("Last login updated for student with ID: {}", id);
    }

    public void deleteStudent(Long id) {
        log.info("Deleting student with ID: {}", id);

        if (!studentRepository.existsById(id)) {
            throw new ValidationException("Student not found with ID: " + id);
        }

        studentRepository.deleteById(id);
        log.info("Student deleted successfully with ID: {}", id);
    }

    @Transactional(readOnly = true)
    public long getStudentCount() {
        return studentRepository.count();
    }

    @Transactional(readOnly = true)
    public long getActiveStudentCount() {
        return studentRepository.countByIsActiveTrue();
    }

    @Transactional(readOnly = true)
    public long getAvailableStudentCount() {
        return studentRepository.countByIsAvailableForHireTrue();
    }

    // Helper methods
    private void validateStudentData(StudentDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new ValidationException("Name is required");
        }

        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }

        if (dto.getQualification() == null || dto.getQualification().trim().isEmpty()) {
            throw new ValidationException("Qualification is required");
        }

        if (dto.getExperienceYears() != null && dto.getExperienceYears() < 0) {
            throw new ValidationException("Experience years cannot be negative");
        }

        if (dto.getExpectedSalary() != null && dto.getExpectedSalary() <= 0) {
            throw new ValidationException("Expected salary must be positive");
        }
    }

    private boolean matchesSearchCriteria(Student student, StudentDTO.StudentSearchDTO searchDto) {
        if (searchDto.getQualification() != null &&
                !student.getQualification().toLowerCase().contains(searchDto.getQualification().toLowerCase())) {
            return false;
        }

        if (searchDto.getLocation() != null &&
                (student.getCurrentLocation() == null ||
                        !student.getCurrentLocation().toLowerCase().contains(searchDto.getLocation().toLowerCase()))) {
            return false;
        }

        if (searchDto.getMinExperience() != null &&
                (student.getExperienceYears() == null || student.getExperienceYears() < searchDto.getMinExperience())) {
            return false;
        }

        if (searchDto.getMaxExperience() != null &&
                (student.getExperienceYears() == null || student.getExperienceYears() > searchDto.getMaxExperience())) {
            return false;
        }

        if (searchDto.getSkills() != null && !searchDto.getSkills().isEmpty()) {
            if (student.getSkills() == null ||
                    !student.getSkills().stream().anyMatch(skill ->
                            searchDto.getSkills().stream().anyMatch(searchSkill ->
                                    skill.toLowerCase().contains(searchSkill.toLowerCase())))) {
                return false;
            }
        }

        if (searchDto.getMinSalary() != null &&
                (student.getExpectedSalary() == null || student.getExpectedSalary() < searchDto.getMinSalary())) {
            return false;
        }

        if (searchDto.getMaxSalary() != null &&
                (student.getExpectedSalary() == null || student.getExpectedSalary() > searchDto.getMaxSalary())) {
            return false;
        }

        if (searchDto.getAvailableForHire() != null &&
                !searchDto.getAvailableForHire().equals(student.getIsAvailableForHire())) {
            return false;
        }

        return true;
    }

    private void updateStudentFields(Student student, StudentDTO dto) {
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setQualification(dto.getQualification());
        student.setResumeURL(dto.getResumeURL());
        student.setProfilePictureURL(dto.getProfilePictureURL());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setCurrentLocation(dto.getCurrentLocation());
        student.setPreferredJobLocation(dto.getPreferredJobLocation());
        student.setExperienceYears(dto.getExperienceYears());
        student.setSkills(dto.getSkills());
        student.setLinkedInProfile(dto.getLinkedInProfile());
        student.setGithubProfile(dto.getGithubProfile());
        student.setPortfolioURL(dto.getPortfolioURL());
        student.setExpectedSalary(dto.getExpectedSalary());
        student.setBio(dto.getBio());
        student.setIsAvailableForHire(dto.getIsAvailableForHire());

        if (dto.getIsActive() != null) {
            student.setIsActive(dto.getIsActive());
        }
    }

    private Student mapToEntity(StudentDTO dto) {
        return Student.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .qualification(dto.getQualification())
                .resumeURL(dto.getResumeURL())
                .profilePictureURL(dto.getProfilePictureURL())
                .dateOfBirth(dto.getDateOfBirth())
                .currentLocation(dto.getCurrentLocation())
                .preferredJobLocation(dto.getPreferredJobLocation())
                .experienceYears(dto.getExperienceYears() != null ? dto.getExperienceYears() : 0)
                .skills(dto.getSkills())
                .linkedInProfile(dto.getLinkedInProfile())
                .githubProfile(dto.getGithubProfile())
                .portfolioURL(dto.getPortfolioURL())
                .expectedSalary(dto.getExpectedSalary())
                .bio(dto.getBio())
                .isAvailableForHire(dto.getIsAvailableForHire() != null ? dto.getIsAvailableForHire() : true)
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
    }

    private StudentDTO mapToDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .name(student.getName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .qualification(student.getQualification())
                .resumeURL(student.getResumeURL())
                .profilePictureURL(student.getProfilePictureURL())
                .dateOfBirth(student.getDateOfBirth())
                .currentLocation(student.getCurrentLocation())
                .preferredJobLocation(student.getPreferredJobLocation())
                .experienceYears(student.getExperienceYears())
                .skills(student.getSkills())
                .linkedInProfile(student.getLinkedInProfile())
                .githubProfile(student.getGithubProfile())
                .portfolioURL(student.getPortfolioURL())
                .expectedSalary(student.getExpectedSalary())
                .isAvailableForHire(student.getIsAvailableForHire())
                .isProfileComplete(student.getIsProfileComplete())
                .lastLoginAt(student.getLastLoginAt())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .bio(student.getBio())
                .isActive(student.getIsActive())
                .build();
    }
}