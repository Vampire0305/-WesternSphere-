package com.jobportal.JobPortal.controller;

import com.jobportal.JobPortal.DTO.ApplicationDTO;
import com.jobportal.JobPortal.Enum.Status;
import com.jobportal.JobPortal.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApplicationDTO> apply(@Valid @RequestBody ApplicationDTO dto) {
        try {
            ApplicationDTO savedApplication = applicationService.apply(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedApplication);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByStudentId(
            @PathVariable Long studentId,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false, defaultValue = "false") Boolean includeArchived) {
        try {
            List<ApplicationDTO> applications = applicationService.getApplicationsByStudentId(studentId, status, includeArchived);
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByJobId(
            @PathVariable Long jobId,
            @RequestParam(required = false) Status status) {
        try {
            List<ApplicationDTO> applications = applicationService.getApplicationsByJobId(jobId, status);
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/paginated")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ApplicationDTO>> getAllApplicationsPaginated(
            Pageable pageable,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long jobId) {
        try {
            Page<ApplicationDTO> applications = applicationService.getAllApplicationsPaginated(pageable, status, studentId, jobId);
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN') or hasRole('RECRUITER')")
    public ResponseEntity<ApplicationDTO> getApplicationById(@PathVariable Long id) {
        try {
            ApplicationDTO application = applicationService.getApplicationById(id);
            return ResponseEntity.ok(application);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApplicationDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam Status status,
            @RequestParam(required = false) String feedback) {
        try {
            ApplicationDTO updatedApplication = applicationService.updateStatus(id, status, feedback);
            return ResponseEntity.ok(updatedApplication);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/archive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApplicationDTO> archiveApplication(@PathVariable Long id) {
        try {
            ApplicationDTO archivedApplication = applicationService.archiveApplication(id);
            return ResponseEntity.ok(archivedApplication);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/unarchive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApplicationDTO> unarchiveApplication(@PathVariable Long id) {
        try {
            ApplicationDTO unarchivedApplication = applicationService.unarchiveApplication(id);
            return ResponseEntity.ok(unarchivedApplication);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        try {
            applicationService.deleteApplication(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statistics/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getStudentApplicationStatistics(@PathVariable Long studentId) {
        try {
            Map<String, Object> statistics = applicationService.getStudentApplicationStatistics(studentId);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statistics/job/{jobId}")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getJobApplicationStatistics(@PathVariable Long jobId) {
        try {
            Map<String, Object> statistics = applicationService.getJobApplicationStatistics(jobId);
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApplicationDTO>> getRecentApplications(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<ApplicationDTO> recentApplications = applicationService.getRecentApplications(limit);
            return ResponseEntity.ok(recentApplications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/withdraw")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApplicationDTO> withdrawApplication(@PathVariable Long id) {
        try {
            ApplicationDTO withdrawnApplication = applicationService.withdrawApplication(id);
            return ResponseEntity.ok(withdrawnApplication);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/{id}/shortlist")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('ADMIN')")
    public ResponseEntity<ApplicationDTO> shortlistApplication(@PathVariable Long id) {
        try {
            ApplicationDTO result = applicationService.shortlistApplication(id);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
