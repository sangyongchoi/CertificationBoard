package com.example.certificationboard.post.application.request

import com.example.certificationboard.post.domain.Contents
import com.example.certificationboard.post.domain.TaskContents
import com.example.certificationboard.post.exception.NotAllowedValueException
import com.example.certificationboard.post.exception.NotSupportFunctionException
import java.time.LocalDateTime
import javax.validation.constraints.*

abstract class TaskRequest(
        open val postId: String
) {
    abstract fun convertToTaskContents(taskContents: Contents): Contents
}

data class TaskManagerRequest(
    @field:NotEmpty override val postId: String
    , val managersInfo: List<ManagerInfo>? = null
): TaskRequest(postId) {

    override fun convertToTaskContents(taskContents: Contents): Contents {
        val managers = managersInfo?.map{ it.id }

        return when (taskContents) {
            is TaskContents -> TaskContents(
                taskContents.title,
                taskContents.taskStatus,
                taskContents.startDate,
                taskContents.endDate,
                managers,
                taskContents.priority,
                taskContents.progress,
                taskContents.taskNumber,
                taskContents.context
            )
            else -> throw NotSupportFunctionException("게시물 타입이 업무에만 지원하는 기능입니다.")
        }
    }
}

data class TaskProgressRequest(
        @field:NotEmpty override val postId: String
        , @field:Min(0) @field:Max(100) val progress: Int
): TaskRequest(postId) {

    override fun convertToTaskContents(taskContents: Contents): Contents {
        return when (taskContents) {
            is TaskContents -> TaskContents(
                taskContents.title,
                taskContents.taskStatus,
                taskContents.startDate,
                taskContents.endDate,
                taskContents.managers,
                taskContents.priority,
                progress,
                taskContents.taskNumber,
                taskContents.context
            )
            else -> throw NotSupportFunctionException("게시물 타입이 업무에만 지원하는 기능입니다.")
        }
    }
}

data class TaskStatusRequest(
        @field:NotEmpty override val postId: String
        , @field:NotEmpty val status: String
): TaskRequest(postId) {

    override fun convertToTaskContents(taskContents: Contents): Contents {
        return when (taskContents) {
            is TaskContents -> TaskContents(
                taskContents.title,
                TaskContents.Status.valueOf(status),
                taskContents.startDate,
                taskContents.endDate,
                taskContents.managers,
                taskContents.priority,
                taskContents.progress,
                taskContents.taskNumber,
                taskContents.context
            )
            else -> throw NotSupportFunctionException("게시물 타입이 업무에만 지원하는 기능입니다.")
        }
    }
}

data class TaskDateRequest(
        @field:NotEmpty override val postId: String
        , val startDate: LocalDateTime?
        , val endDate: LocalDateTime?
): TaskRequest(postId) {

    init {
        if (startDate != null && endDate != null) {
            if (startDate > endDate) {
                throw NotAllowedValueException("시작일자는 종료일자보다 이후 날짜로 설정할 수 없습니다.")
            }
        }
    }

    override fun convertToTaskContents(taskContents: Contents): Contents {
        return when (taskContents) {
            is TaskContents -> TaskContents(
                taskContents.title,
                taskContents.taskStatus,
                startDate,
                endDate,
                taskContents.managers,
                taskContents.priority,
                taskContents.progress,
                taskContents.taskNumber,
                taskContents.context
            )
            else -> throw NotSupportFunctionException("게시물 타입이 업무에만 지원하는 기능입니다.")
        }
    }
}

data class TaskModifyRequest(
    @field:NotEmpty override val postId: String
    , @field:NotBlank val title: String
    , @field:NotEmpty val taskStatus: String
    , val startDate: LocalDateTime?
    , val endDate: LocalDateTime?
    , val managersInfo: List<ManagerInfo>?
    , val progress: Int
    , val priority: String
    , @field:NotBlank val context: String
): TaskRequest(postId) {

    override fun convertToTaskContents(taskContents: Contents): Contents {
        val managers = managersInfo?.map { it.id }

        return when (taskContents) {
            is TaskContents -> TaskContents(
                title,
                TaskContents.Status.valueOf(taskStatus),
                startDate,
                endDate,
                managers,
                TaskContents.Priority.valueOf(priority),
                progress,
                taskContents.taskNumber,
                context
            )
            else -> throw NotSupportFunctionException("게시물 타입이 업무에만 지원하는 기능입니다.")
        }
    }

}