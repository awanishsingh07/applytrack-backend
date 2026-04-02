package com.awanish.applytrack_backend.controller;

import com.awanish.applytrack_backend.entity.Application;
import com.awanish.applytrack_backend.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;
    // ✅ No userId in path — extracted from JWT token
    @PostMapping("/{resumeId}")
    public ResponseEntity<Application> addApplication(
            @PathVariable Long resumeId,
            @RequestBody Application app,
            Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.addApplication(email, app, resumeId));
    }

    @GetMapping
    public ResponseEntity<List<Application>> getApplications(Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.getApplicationsByEmail(email));
    }

    // ✅ Email passed to service for ownership check
    @PutMapping("/{id}")
    public ResponseEntity<Application> updateApplication(
            @PathVariable Long id,
            @RequestBody Application app,
            Authentication authentication) {

        String email = authentication.getName();
        return ResponseEntity.ok(applicationService.updateApplication(id, email, app));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Long id,
            Authentication authentication) {
        applicationService.deleteApplication(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}

