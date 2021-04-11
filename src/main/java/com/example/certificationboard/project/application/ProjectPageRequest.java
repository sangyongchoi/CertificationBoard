package com.example.certificationboard.project.application;

import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotEmpty;

public class ProjectPageRequest {

    @NotEmpty
    private String memberId;
    private Pageable pageable;

    public ProjectPageRequest(String memberId, Pageable pageable) {
        this.memberId = memberId;
        this.pageable = pageable;
    }

    public String getMemberId() {
        return memberId;
    }

    public Pageable getPageable() {
        return pageable;
    }

    @Override
    public String toString() {
        return "ProjectPageRequest{" +
                "memberId='" + memberId + '\'' +
                ", pageable=" + pageable +
                '}';
    }
}
