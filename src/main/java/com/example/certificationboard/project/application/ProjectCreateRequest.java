package com.example.certificationboard.project.application;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.project.domain.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCreateRequest {

    @NotEmpty
    private String userId;
    @NotEmpty
    private String title;
    private String explain;

    public Project toProjectEntity(Member member) {
        return new Project(member.getOrganizationId(), member.getId(), title, explain);
    }
}
