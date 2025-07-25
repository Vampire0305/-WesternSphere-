package com.jobportal.JobPortal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.jobportal.JobPortal.service.FileUploadService;


@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/resume")
    public ResponseEntity<String> uploadResume(@RequestParam("file")MultipartFile file){
        try {
            String url =fileUploadService.uploadFile(file);
            return ResponseEntity.ok(url);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload failed"+ e.getMessage());
        }
    }
}
