package com.example.certificationboard.project.application;

import com.example.certificationboard.project.domain.Project;

public class ProjectDto {
    private Long id;
    private String title;
    private String explain;

    public ProjectDto(Long id, String title, String explain) {
        this.id = id;
        this.title = title;
        this.explain = explain;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getExplain() {
        return explain;
    }

    public static ProjectDto of(Project project) {
        return new ProjectDto(project.getId(), project.getTitle(), project.getExplain());
    }

}
