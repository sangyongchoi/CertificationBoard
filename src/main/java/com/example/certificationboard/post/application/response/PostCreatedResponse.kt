package com.example.certificationboard.post.application.response

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class PostCreatedResponse(
        val id: String,
        val taskNumber: Long,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        val createdAt: LocalDateTime
) {
}