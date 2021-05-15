package com.example.certificationboard.projectparticipants.presentation

import com.example.certificationboard.common.config.argument.resolver.AuthUser
import com.example.certificationboard.member.domain.Member
import com.example.certificationboard.projectparticipants.application.FavoriteDto
import com.example.certificationboard.projectparticipants.application.ProjectParticipantsDto
import com.example.certificationboard.projectparticipants.application.ProjectParticipantsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class ProjectParticipantsController (
    private var projectParticipantsService: ProjectParticipantsService
){

    @GetMapping("/participants/{projectId}")
    fun getParticipantsList(@PathVariable projectId: Long): List<ProjectParticipantsDto> {
        return projectParticipantsService.getProjectParticipantsList(projectId)
    }

    @PostMapping("/favorite")
    fun addFavorite(@AuthUser user: Member, @RequestBody request: FavoriteDto): ResponseEntity<String> {
        projectParticipantsService.addFavorite(user, request.projectId)

        return ResponseEntity.ok()
            .body("success")
    }

    @DeleteMapping("/favorite")
    fun deleteFavorite(@AuthUser user: Member, @RequestBody request: FavoriteDto): ResponseEntity<String> {
        projectParticipantsService.addFavorite(user, request.projectId)

        return ResponseEntity.ok()
            .body("success")
    }
}