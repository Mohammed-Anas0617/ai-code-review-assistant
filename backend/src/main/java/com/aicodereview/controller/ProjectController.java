package com.aicodereview.controller;

import com.aicodereview.dto.ProjectResponse;
import com.aicodereview.entity.Project;
import com.aicodereview.entity.Review;
import com.aicodereview.entity.ReviewFinding;
import com.aicodereview.entity.User;
import com.aicodereview.repository.ProjectRepository;
import com.aicodereview.repository.ReviewFindingRepository;
import com.aicodereview.repository.ReviewRepository;
import com.aicodereview.repository.UserRepository;
import com.aicodereview.service.FileStorageService;
import org.checkerframework.checker.units.qual.Acceleration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final FileStorageService fileStorageService;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewFindingRepository reviewFindingRepository;

    public ProjectController(FileStorageService fileStorageService,
                             ProjectRepository projectRepository,
                             UserRepository userRepository) {
        this.fileStorageService = fileStorageService;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/upload")
    public ProjectResponse uploadFile(@RequestParam("file") MultipartFile file,
                                      Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = new Project();
        project.setUser(user);
        project.setProjectName(file.getOriginalFilename());
        project.setUploadType("FILE");
        projectRepository.save(project);
        String savedPath = fileStorageService.storeFile(file); // adjust to match your actual method name/signature
        project.setFilePath(savedPath);

        Project saved = projectRepository.save(project);

        return new ProjectResponse(
                saved.getId(),
                saved.getProjectName(),
                saved.getUploadType(),
                saved.getCreatedAt()
        );
    }

    @GetMapping
    public List<ProjectResponse> getMyProjects(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Project> projects = projectRepository.findByUser(user);

        return projects.stream()
                .map(p -> new ProjectResponse(p.getId(), p.getProjectName(), p.getUploadType(), p.getCreatedAt()))
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        List<Review> reviews = reviewRepository.findByProjectId(id);
        for (Review review : reviews) {
            List<ReviewFinding> findings = reviewFindingRepository.findByReviewId(review.getId());
            reviewFindingRepository.deleteAll(findings);
        }
        reviewRepository.deleteAll(reviews);

        projectRepository.delete(project);
        return ResponseEntity.noContent().build();
    }
}