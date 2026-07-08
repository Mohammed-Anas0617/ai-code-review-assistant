package com.aicodereview.dto;

import java.time.LocalDateTime;

public class ProjectResponse {

    private Long id;
    private String projectName;
    private String uploadType;
    private LocalDateTime createdAt;

    public ProjectResponse(Long id, String projectName, String uploadType, LocalDateTime createdAt) {
        this.id = id;
        this.projectName = projectName;
        this.uploadType = uploadType;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getProjectName() { return projectName; }
    public String getUploadType() { return uploadType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}