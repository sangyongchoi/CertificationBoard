package com.example.certificationboard.project.presentation;

import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.project.application.*;
import com.example.certificationboard.project.domain.Project;
import com.example.certificationboard.projectparticipants.application.ProjectParticipantsService;
import com.example.certificationboard.projectparticipants.domain.ProjectParticipants;
import com.example.certificationboard.projectparticipants.domain.ProjectParticipantsId;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        projectParticipantsService.join(projectParticipantsId, ProjectParticipants.Role.ADMIN);

        return new ProjectCreateResponse(createdProject.getId());
    }

    @PostMapping(value = "/project/invite")
    public ResponseEntity<String> invite(@RequestBody @Valid ProjectInviteRequest inviteRequest) {
        projectParticipantsService.invite(inviteRequest);

        return ResponseEntity.ok("success");
    }

    @GetMapping(value = "/normal")
    public ProjectPageResponse normalList(Pageable pageable, String userId) {
        return projectService.list(pageable, userId, false);
    }

    @GetMapping(value = "/favorite")
    public ProjectPageResponse favoriteList(Pageable pageable, String userId) {
        return projectService.list(pageable, userId, true);
    }

    @GetMapping("/project/info/{projectId}")
    public ProjectInfoResponse getProjectInfo(@PathVariable("projectId") Long projectId) {
        final Project project = projectService.findById(projectId);

        return new ProjectInfoResponse(project.getTitle(), project.getExplain(), project.getCreatedDate());
    }

}
