package com.awanish.applytrack_backend.service;

import com.awanish.applytrack_backend.entity.Application;
import com.awanish.applytrack_backend.entity.Resume;
import com.awanish.applytrack_backend.entity.User;
import com.awanish.applytrack_backend.exception.ResourceNotFoundException;
import com.awanish.applytrack_backend.repository.ApplicationRepository;
import com.awanish.applytrack_backend.repository.ResumeRepository;
import com.awanish.applytrack_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    // ✅ No more userId from path — email comes from JWT
    public Application addApplication(String email, Application app, Long resumeId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        // ✅ Only allow using resumes that belong to this user
        if (!resume.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Resume does not belong to this user");
        }

        app.setUser(user);
        app.setResume(resume);
        app.setApplyDate(LocalDate.now());
        app.setStatus("Applied");

        return applicationRepository.save(app);
    }

    public List<Application> getApplicationsByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return applicationRepository.findByUserId(user.getId());
    }

    public void deleteApplication(Long id, String email) {
        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        if (!app.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }
        applicationRepository.deleteById(id);
    }

    // ✅ Ownership check added
    public Application updateApplication(Long id, String email, Application updated) {

        Application app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        if (!app.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to update this application");
        }
        app.setStatus(updated.getStatus());
        app.setNotes(updated.getNotes());

        return applicationRepository.save(app);
    }
}