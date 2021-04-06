package com.example.certificationboard.project.application;

public class ProjectCreateResponse {
    private final Long projectId;

    public ProjectCreateResponse(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectId() {
        return projectId;
    }
}
