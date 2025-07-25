package com.jobportal.JobPortal.controller;

import com.jobportal.JobPortal.DTO.StudentDTO;
import com.jobportal.JobPortal.repository.StudentRepository;
import com.jobportal.JobPortal.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;
    private final StudentRepository studentRepository;

    // ────────────────────────────── CRUD OPERATIONS ──────────────────────────────

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STUDENT')")
    public ResponseEntity<StudentDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        log.info("Creating student: {}", studentDTO.getEmail());
        return new ResponseEntity<>(studentService.createStudent(studentDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #id == authentication.principal.id)")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable @NotNull Long id,
                                                    @Valid @RequestBody StudentDTO studentDTO) {
        log.info("Updating student: {}", id);
        return ResponseEntity.ok(studentService.updateStudent(id, studentDTO));
    }

    @PatchMapping("/{id}/profile")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #id == authentication.principal.id)")
    public ResponseEntity<StudentDTO> updateProfile(@PathVariable @NotNull Long id,
                                                    @Valid @RequestBody StudentDTO.StudentProfileUpdateDTO profileUpdateDTO) {
        log.info("Partial update of student profile: {}", id);
        return ResponseEntity.ok(studentService.updateStudentProfile(id, profileUpdateDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable @NotNull Long id) {
        log.info("Deleting student: {}", id);
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // ────────────────────────────── GET BY IDENTIFIERS ──────────────────────────────

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER') or hasRole('STUDENT')")
    public ResponseEntity<StudentDTO> getById(Authentication authentication, @PathVariable @NotNull Long id) {
        StudentDTO student = studentService.getStudentById(id);
        String emailFromJWT = authentication.getName(); // sub from JWT

        boolean isAdminOrRecruiter = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_RECRUITER"));

        if (isAdminOrRecruiter || student.getEmail().equals(emailFromJWT)) {
            log.info("Fetching student by ID: {}", id);
            return ResponseEntity.ok(student);
        }

        return ResponseEntity.status(403).build(); // Forbidden for other students
    }


    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER')")
    public ResponseEntity<StudentDTO> getByEmail(@PathVariable @NotBlank String email) {
        log.info("Fetching student by email: {}", email);
        return ResponseEntity.ok(studentService.getStudentByEmail(email));
    }

    @GetMapping("/find/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER')")
    public ResponseEntity<Optional<StudentDTO>> findByEmail(@PathVariable @NotBlank String email) {
        log.info("Searching student by email: {}", email);
        return ResponseEntity.ok(studentService.findStudentByEmail(email));
    }

    // ────────────────────────────── PAGINATION + FILTERS ──────────────────────────────

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER')")
    public ResponseEntity<Page<StudentDTO>> getAll(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                   @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
                                                   @RequestParam(defaultValue = "id") String sortBy,
                                                   @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Fetching all students - page: {}, size: {}", page, size);
        return ResponseEntity.ok(studentService.getAllStudents(page, size, sortBy, sortDir));
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER')")
    public ResponseEntity<Page<StudentDTO>> getActive(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                      @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        log.info("Fetching active students - page: {}", page);
        return ResponseEntity.ok(studentService.getActiveStudents(page, size));
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER')")
    public ResponseEntity<Page<StudentDTO>> getAvailable(@RequestParam(defaultValue = "0") @Min(0) int page,
                                                         @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        log.info("Fetching available students - page: {}", page);
        return ResponseEntity.ok(studentService.getAvailableStudents(page, size));
    }

    @PostMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER')")
    public ResponseEntity<List<StudentDTO>> search(@Valid @RequestBody StudentDTO.StudentSearchDTO searchDTO) {
        log.info("Searching students: {}", searchDTO);
        return ResponseEntity.ok(studentService.searchStudents(searchDTO));
    }

    // ────────────────────────────── FILTERS ──────────────────────────────

    @GetMapping("/qualification/{qualification}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER')")
    public ResponseEntity<List<StudentDTO>> getByQualification(@PathVariable @NotBlank String qualification) {
        log.info("Fetching students with qualification: {}", qualification);
        return ResponseEntity.ok(studentService.getStudentsByQualification(qualification));
    }

    @GetMapping("/location/{location}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER')")
    public ResponseEntity<List<StudentDTO>> getByLocation(@PathVariable @NotBlank String location) {
        log.info("Fetching students from location: {}", location);
        return ResponseEntity.ok(studentService.getStudentsByLocation(location));
    }

    @PostMapping("/skills")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER')")
    public ResponseEntity<List<StudentDTO>> getBySkills(@RequestBody @NotNull Set<String> skills) {
        log.info("Fetching students with skills: {}", skills);
        return ResponseEntity.ok(studentService.getStudentsBySkills(skills));
    }

    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RECRUITER')")
    public ResponseEntity<List<StudentDTO>> getRecent(@RequestParam(defaultValue = "10") @Min(1) @Max(50) int limit) {
        log.info("Fetching recent students: limit={}", limit);
        return ResponseEntity.ok(studentService.getRecentlyRegisteredStudents(limit));
    }

    // ────────────────────────────── STATUS UPDATES ──────────────────────────────

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #id == authentication.principal.id)")
    public ResponseEntity<StudentDTO> deactivate(@PathVariable @NotNull Long id) {
        log.info("Deactivating student: {}", id);
        return ResponseEntity.ok(studentService.deactivateStudent(id));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StudentDTO> activate(@PathVariable @NotNull Long id) {
        log.info("Activating student: {}", id);
        return ResponseEntity.ok(studentService.activateStudent(id));
    }

    @PutMapping("/{id}/last-login")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('STUDENT') and #id == authentication.principal.id)")
    public ResponseEntity<Void> updateLastLogin(@PathVariable @NotNull Long id) {
        log.info("Updating last login for student: {}", id);
        studentService.updateLastLogin(id);
        return ResponseEntity.ok().build();
    }

    // ────────────────────────────── STATS ──────────────────────────────

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Long>> getStats() {
        log.info("Fetching student stats");
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalStudents", studentService.getStudentCount());
        stats.put("activeStudents", studentService.getActiveStudentCount());
        stats.put("availableStudents", studentService.getAvailableStudentCount());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> count() {
        log.info("Fetching total student count");
        return ResponseEntity.ok(studentService.getStudentCount());
    }

    @GetMapping("/count/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countActive() {
        log.info("Fetching active student count");
        return ResponseEntity.ok(studentService.getActiveStudentCount());
    }

    @GetMapping("/count/available")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countAvailable() {
        log.info("Fetching available student count");
        return ResponseEntity.ok(studentService.getAvailableStudentCount());
    }

    // ────────────────────────────── HEALTH ──────────────────────────────

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "StudentController");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/internal/count")
    @PreAuthorize("hasRole('ADMIN')") // Only users with the ADMIN role can access this
    public ResponseEntity<Long> countInternal() {
        try {
            Long count = studentRepository.count();
            // Return a 200 OK status with the count in the body
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            // Handle any database or repository exceptions gracefully
            // Return a 500 Internal Server Error with a more informative message
            // You can also log the exception here for debugging
            System.err.println("Error while counting student: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0L); // Or just return 0
        }
    }
}
