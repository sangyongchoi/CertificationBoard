package com.example.certificationboard.post.application.response

import com.example.certificationboard.post.domain.Contents
import com.example.certificationboard.post.domain.TaskContents
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class TaskContentsDto(
    val title: String,
    val taskStatus: TaskContents.Status,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    val startDate: LocalDateTime? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    val endDate: LocalDateTime? = null,
    val managers: List<ManagerInfo>,
    var priority: TaskContents.Priority?,
    var progress: Int?,
    var taskNumber: Long,
    var context: String
): Contents {
    constructor(contents: TaskContents, managers: List<ManagerInfo>) : this(
        contents.title,
        contents.taskStatus,
        contents.startDate,
        contents.endDate,
        managers,
        contents.priority ?: TaskContents.Priority.NORMAL,
        contents.progress ?: 0,
        contents.taskNumber,
        contents.context
    )
}

data class ManagerInfo(
        val id: String
        , val name: String
){
}

