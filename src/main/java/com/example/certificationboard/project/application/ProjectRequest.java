package com.example.certificationboard.project.application;

import javax.validation.constraints.NotNull;

public class ProjectRequest {

    @NotNull
    private Long id;

    public ProjectRequest() {
    }

    public ProjectRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}
