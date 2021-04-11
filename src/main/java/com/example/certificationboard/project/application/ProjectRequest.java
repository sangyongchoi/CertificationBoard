package com.example.certificationboard.project.application;

import javax.validation.constraints.NotNull;

public class ProjectRequest {

    @NotNull
    private Long projectId;
    private String memberId;

    public ProjectRequest() {
    }

    public ProjectRequest(@NotNull Long projectId, String memberId) {
        this.projectId = projectId;
        this.memberId = memberId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public String getMemberId() {
        return memberId;
    }
}
