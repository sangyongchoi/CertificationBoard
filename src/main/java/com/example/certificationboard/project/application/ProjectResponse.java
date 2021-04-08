package com.example.certificationboard.project.application;

import com.example.certificationboard.project.domain.Project;

public class ProjectResponse {
    private Long id;
    private String title;
    private String explain;

    public ProjectResponse(Long id, String title, String explain) {
        this.id = id;
        this.title = title;
        this.explain = explain;
    }

    public Long getId() {
        return id;
    }

    public static ProjectResponse of(Project project) {
        return new ProjectResponse(project.getId(), project.getTitle(), project.getExplain());
    }

}
