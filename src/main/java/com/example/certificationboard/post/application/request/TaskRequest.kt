package com.example.certificationboard.post.application.request

import com.example.certificationboard.post.domain.Post
import com.example.certificationboard.post.domain.TaskContents
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

data class TaskRequest(
        @field:NotBlank val title: String
        , val startDate: LocalDateTime
        , val endDate: LocalDateTime
        , @field:NotBlank val userId: String
        , @field:Positive val projectId: Long
) {

    fun convertToPostEntity(): Post{
        return Post(projectId, userId, Post.Type.TASK, TaskContents(title, startDate, endDate))
    }
}