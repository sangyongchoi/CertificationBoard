package com.example.certificationboard.reply.application

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ReplyResponse(
    val id: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    val createdAt: LocalDateTime
) {
}