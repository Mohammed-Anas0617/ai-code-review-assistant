package com.aicodereview.controller;

import com.aicodereview.dto.ProjectResponse;
import com.aicodereview.entity.Project;
import com.aicodereview.entity.User;
import com.aicodereview.repository.ProjectRepository;
import com.aicodereview.repository.UserRepository;
import com.aicodereview.service.FileStorageService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final FileStorageService fileStorageService;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

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

        // Store the file on disk
        fileStorageService.storeFile(file);

        // Save project record in DB
        Project project = new Project();
        project.setUser(user);
        project.setProjectName(file.getOriginalFilename());
        project.setUploadType("FILE");

        Project saved = projectRepository.save(project);

        return new ProjectResponse(
                saved.getId(),
                saved.getProjectName(),
                saved.getUploadType(),
                saved.getCreatedAt()
        );
    }
}