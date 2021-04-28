package com.example.certificationboard.post.application.response

import com.example.certificationboard.post.domain.Contents
import com.example.certificationboard.post.domain.TaskContents
import java.time.LocalDateTime

data class TaskContentsDto(
        val title: String
        , val taskStatus: TaskContents.Status
        , val startDate: LocalDateTime? = null
        , val endDate: LocalDateTime? = null
        , val managers: List<ManagerInfo>
        , var priority: TaskContents.Priority?
        , var progress: Int?
        , var taskNumber: Long
): Contents {
    constructor(contents: TaskContents, managers: List<ManagerInfo>): this(
        contents.title
        , contents.taskStatus
        , contents.startDate
        , contents.endDate
        , managers
        , contents.priority ?: TaskContents.Priority.NORMAL
        , contents.progress ?: 0
        , contents.taskNumber
    )
}

data class ManagerInfo(
        val id: String
        , val name: String
){
}

