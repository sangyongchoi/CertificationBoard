package com.example.certificationboard.project.application;

import java.util.List;

public class ProjectPageResponse {
    private boolean hasNext;
    private List<ProjectInfo> projectList;

    public boolean isHasNext() {
        return hasNext;
    }

    public List<ProjectInfo> getProjectList() {
        return projectList;
    }

    public ProjectPageResponse(boolean hasNext, List<ProjectInfo> projectList) {
        this.hasNext = hasNext;
        this.projectList = projectList;
    }
}
