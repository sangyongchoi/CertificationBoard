package com.example.certificationboard.project.application

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ProjectInfoResponse(
    val title: String,
    val explain: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime
) {
}