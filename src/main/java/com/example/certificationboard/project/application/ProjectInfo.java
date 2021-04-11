package com.example.certificationboard.project.application;

import com.example.certificationboard.project.domain.Project;

public class ProjectInfo {
    private Long id;
    private String title;
    private String explain;

    public ProjectInfo(Long id, String title, String explain) {
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

    public static ProjectInfo of(Project project) {
        return new ProjectInfo(project.getId(), project.getTitle(), project.getExplain());
    }

    @Override
    public String toString() {
        return "ProjectInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", explain='" + explain + '\'' +
                '}';
    }
}
