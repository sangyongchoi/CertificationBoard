package com.example.certificationboard.project.application;

import java.util.List;

public class ProjectResponse {
    private boolean hasNext;
    private List<ProjectDto> projectList;

    public boolean isHasNext() {
        return hasNext;
    }

    public List<ProjectDto> getProjectList() {
        return projectList;
    }

    public ProjectResponse(boolean hasNext, List<ProjectDto> projectList) {
        this.hasNext = hasNext;
        this.projectList = projectList;
    }
}
