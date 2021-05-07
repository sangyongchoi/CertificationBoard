package com.example.certificationboard.project.application

import javax.validation.constraints.NotEmpty

data class ProjectInviteRequest(
    val projectId: Long,
    @field:NotEmpty val inviteeId: String,
    @field:NotEmpty val inviterId: String
) {
}