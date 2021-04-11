package com.example.certificationboard.project.presentation;

import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.project.application.*;
import com.example.certificationboard.project.domain.Project;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectParticipantsService projectParticipantsService;
    private final MemberService memberService;

    public ProjectController(ProjectService projectService, ProjectParticipantsService projectParticipantsService, MemberService memberService) {
        this.projectService = projectService;
        this.projectParticipantsService = projectParticipantsService;
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

//    @GetMapping(value = "/normal")
//    public ProjectPageResponse normalList(@RequestBody @Valid ProjectPageRequest pageRequest) {
//        return projectService.list(pageable, false);
//    }
//
//    @GetMapping(value = "/favorite")
//    public ProjectPageResponse favoriteList(@RequestBody @Valid ProjectPageRequest pageRequest) {
//        return projectService.list(pageable, true);
//    }

}
