package com.example.certificationboard.project.presentation;

import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.project.application.*;
import com.example.certificationboard.project.domain.Project;
import com.example.certificationboard.projectparticipants.application.ProjectParticipantsService;
import com.example.certificationboard.projectparticipants.domain.ProjectParticipants;
import com.example.certificationboard.projectparticipants.domain.ProjectParticipantsId;
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
    private final ProjectParticipantsService projectParticipantsService;

    public ProjectController(ProjectService projectService, MemberService memberService, ProjectParticipantsService projectParticipantsService) {
        this.projectService = projectService;
        this.memberService = memberService;
        this.projectParticipantsService = projectParticipantsService;
    }

    @PostMapping(value = "/project")
    public ProjectCreateResponse createProject(@RequestBody @Valid ProjectCreateRequest projectCreateRequest) {
        String userId = projectCreateRequest.getUserId();
        final Member member = memberService.findById(userId);
        final Project project = projectCreateRequest.toProjectEntity(member);

        final Project createdProject = projectService.create(project);
        final ProjectParticipantsId projectParticipantsId = new ProjectParticipantsId(createdProject, member);
        final ProjectParticipants projectParticipants = new ProjectParticipants(projectParticipantsId, ProjectParticipants.Role.ADMIN, false);

        projectParticipantsService.join(projectParticipants);

        return new ProjectCreateResponse(createdProject.getId());
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
