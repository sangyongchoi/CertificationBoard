package com.example.certificationboard.project.presentation;

import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.project.application.ProjectCreateRequest;
import com.example.certificationboard.project.application.ProjectService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectController {

    private final ProjectService projectService;
    private final MemberService memberService;

    public ProjectController(ProjectService projectService, MemberService memberService) {
        this.projectService = projectService;
        this.memberService = memberService;
    }

    @PostMapping(value = "/project")
    public Long createProject(@RequestBody ProjectCreateRequest projectCreateRequest) {

        return 1L;
    }
}
