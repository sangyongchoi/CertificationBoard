package com.example.certificationboard.post.application.request

import com.example.certificationboard.post.domain.Contents
import com.example.certificationboard.post.domain.TaskContents
import com.example.certificationboard.post.exception.NotSupportFunction
import java.time.LocalDate
import java.time.LocalDateTime
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

abstract class TaskModifyRequest(
        open val postId: String
) {
    abstract fun convertToTaskContents(taskContents: Contents): Contents
}

data class TaskProgressRequest(
        @field:NotEmpty override val postId: String
        , @field:Min(0) @field:Max(100) val progress: Int
): TaskModifyRequest(postId) {

    override fun convertToTaskContents(taskContents: Contents): Contents {
        return when (taskContents) {
            is TaskContents -> TaskContents(
                    taskContents.title
                    , taskContents.taskStatus
                    , taskContents.startDate
                    , taskContents.endDate
                    , taskContents.managers
                    , taskContents.priority
                    , progress
            )
            else -> throw NotSupportFunction("게시물 타입이 업무에만 지원하는 기능입니다.")
        }
    }
}

data class TaskStatusRequest(
        @field:NotEmpty override val postId: String
        , @field:NotEmpty val status: String
): TaskModifyRequest(postId) {

    override fun convertToTaskContents(taskContents: Contents): Contents {
        return when (taskContents) {
            is TaskContents -> TaskContents(
                    taskContents.title
                    , TaskContents.Status.valueOf(status)
                    , taskContents.startDate
                    , taskContents.endDate
                    , taskContents.managers
                    , taskContents.priority
                    , taskContents.progress
            )
            else -> throw NotSupportFunction("게시물 타입이 업무에만 지원하는 기능입니다.")
        }
    }
}

data class TaskDateRequest(
        @field:NotEmpty override val postId: String
        , val startDate: LocalDateTime?
        , val endDate: LocalDateTime?
): TaskModifyRequest(postId) {

    override fun convertToTaskContents(taskContents: Contents): Contents {
        return when (taskContents) {
            is TaskContents -> TaskContents(
                    taskContents.title
                    , taskContents.taskStatus
                    , startDate
                    , endDate
                    , taskContents.managers
                    , taskContents.priority
                    , taskContents.progress
            )
            else -> throw NotSupportFunction("게시물 타입이 업무에만 지원하는 기능입니다.")
        }
    }
}