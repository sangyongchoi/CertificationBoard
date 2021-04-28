package com.example.certificationboard.post.application.request

import com.example.certificationboard.post.domain.Contents
import com.example.certificationboard.post.domain.TaskContents
import com.example.certificationboard.post.exception.NotAllowedValueException
import com.example.certificationboard.post.exception.NotSupportFunctionException
import java.time.LocalDateTime
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty

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
                    ,taskContents.taskNumber
            )
            else -> throw NotSupportFunctionException("게시물 타입이 업무에만 지원하는 기능입니다.")
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
                taskContents.title,
                TaskContents.Status.valueOf(status),
                taskContents.startDate,
                taskContents.endDate,
                taskContents.managers,
                taskContents.priority,
                taskContents.progress,
                taskContents.taskNumber
            )
            else -> throw NotSupportFunctionException("게시물 타입이 업무에만 지원하는 기능입니다.")
        }
    }
}

data class TaskDateRequest(
        @field:NotEmpty override val postId: String
        , val startDate: LocalDateTime?
        , val endDate: LocalDateTime?
): TaskModifyRequest(postId) {

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
                taskContents.taskNumber
            )
            else -> throw NotSupportFunctionException("게시물 타입이 업무에만 지원하는 기능입니다.")
        }
    }
}