package com.example.certificationboard.projectparticipants.application

data class ProjectParticipantsDto(
        val userId: String,
        val name: String
)

data class FavoriteDto(
        val projectId: Long
)