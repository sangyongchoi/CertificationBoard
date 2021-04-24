package com.example.certificationboard.projectparticipants.presentation

import com.example.certificationboard.projectparticipants.application.ProjectParticipantsDto
import com.example.certificationboard.projectparticipants.application.ProjectParticipantsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProjectParticipantsController (
    private var projectParticipantsService: ProjectParticipantsService
){

    @GetMapping("/participants/{projectId}")
    fun getParticipantsList(@PathVariable projectId: Long): List<ProjectParticipantsDto> {
        return projectParticipantsService.getProjectParticipantsList(projectId)
    }
}