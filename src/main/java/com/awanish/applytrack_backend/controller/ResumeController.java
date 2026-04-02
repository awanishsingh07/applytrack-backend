package com.awanish.applytrack_backend.controller;

import com.awanish.applytrack_backend.entity.Resume;
import com.awanish.applytrack_backend.entity.User;
import com.awanish.applytrack_backend.repository.ResumeRepository;
import com.awanish.applytrack_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    // ✅ Get all resumes for logged-in user
    @GetMapping
    public ResponseEntity<List<Resume>> getResumes(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(resumeRepository.findByUserId(user.getId()));
    }

    // ✅ Add resume for logged-in user
    @PostMapping
    public ResponseEntity<Resume> addResume(
            @RequestBody Resume resume,
            Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        resume.setUser(user);
        return ResponseEntity.ok(resumeRepository.save(resume));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(
            @PathVariable Long id,
            Authentication authentication) {
        String email = authentication.getName();
        Resume resume = resumeRepository.findById(id).orElseThrow();
        if (!resume.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }
        resumeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resume> updateResume(
            @PathVariable Long id,
            @RequestBody Resume updated,
            Authentication authentication) {
        String email = authentication.getName();
        Resume resume = resumeRepository.findById(id).orElseThrow();
        if (!resume.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }
        resume.setTag(updated.getTag());
        resume.setFileUrl(updated.getFileUrl());
        return ResponseEntity.ok(resumeRepository.save(resume));
    }
}