package com.example.certificationboard.project.presentation;

import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.project.application.*;
import com.example.certificationboard.project.domain.Project;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ProjectResponse normalList(Pageable pageable) {
        return projectService.list(pageable, false);
    }

    @GetMapping(value = "/favorite")
    public ProjectResponse favoriteList(Pageable pageable) {
        return projectService.list(pageable, true);
    }

    @PostMapping(value = "/favorite")
    public ResponseEntity<Boolean> addFavorite(@RequestBody @Valid ProjectRequest projectRequest){
        final Project project = projectService.findById(projectRequest.getId());
        final boolean isFavorite = projectService.addFavorite(project);

        return ResponseEntity.ok(isFavorite);
    }

    @DeleteMapping(value = "/favorite")
    public ResponseEntity<Boolean> deleteFavorite(@RequestBody @Valid ProjectRequest projectRequest){
        final Project project = projectService.findById(projectRequest.getId());
        final boolean isFavorite = projectService.addFavorite(project);

        return ResponseEntity.ok(isFavorite);
    }
}
