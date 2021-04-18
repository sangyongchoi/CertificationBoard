package com.example.certificationboard.project.presentation;

import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.project.application.*;
import com.example.certificationboard.project.domain.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ProjectController {

    private final ProjectService projectService;
    private final MemberService memberService;

    public ProjectController(ProjectService projectService, MemberService memberService) {
        this.projectService = projectService;
        this.memberService = memberService;
    }

    @PostMapping(value = "/project")
    public ProjectCreateResponse createProject(@RequestBody @Valid ProjectCreateRequest projectCreateRequest) {
        String userId = projectCreateRequest.getUserId();
        final Member member = memberService.findMemberById(userId);
        final Project project = projectCreateRequest.toProjectEntity(member);

        final Long projectId = projectService.create(project, member);

        return new ProjectCreateResponse(projectId);
    }

    @GetMapping(value = "/normal")
    public ProjectPageResponse normalList(Pageable pageable, String userId) {
        return projectService.list(pageable, userId, false);
    }

    @GetMapping(value = "/favorite")
    public ProjectPageResponse favoriteList(Pageable pageable, String userId) {
        return projectService.list(pageable, userId, true);
    }

}
