package com.example.certificationboard.post.application.request

import com.example.certificationboard.post.domain.Post
import com.example.certificationboard.post.domain.TaskContents
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive

data class TaskCreateRequest(
        @field:NotBlank val title: String
        , @field:NotEmpty val taskStatus: String
        , val startDate: LocalDateTime?
        , val endDate: LocalDateTime?
        , val managers: List<String>?
        , val progress: Int
        , val priority: String
        , @field:NotBlank val userId: String
        , @field:Positive val projectId: Long
        , @field:NotBlank val context: String
) {

    fun convertToPostEntity(taskNumber: Long): Post{
        return Post(projectId
                , userId
                , Post.Type.TASK
                , TaskContents(title, TaskContents.Status.valueOf(taskStatus)
                , startDate, endDate, managers
                , TaskContents.Priority.valueOf(priority), progress, taskNumber, context)
        )
    }
}