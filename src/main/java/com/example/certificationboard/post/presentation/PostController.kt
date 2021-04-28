package com.example.certificationboard.post.presentation

import com.example.certificationboard.common.sequence.SequenceCreator
import com.example.certificationboard.post.application.PostService
import com.example.certificationboard.post.application.request.TaskDateRequest
import com.example.certificationboard.post.application.request.TaskProgressRequest
import com.example.certificationboard.post.application.request.TaskRequest
import com.example.certificationboard.post.application.request.TaskStatusRequest
import com.example.certificationboard.post.application.response.PostCreatedResponse
import com.example.certificationboard.post.application.response.PostListResponse
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class PostController(
        private val postService: PostService
        , private val sequenceCreator: SequenceCreator
) {

    @GetMapping("/posts/{projectId}")
    fun list(pageable: Pageable, userId:String,@PathVariable projectId: Long): PostListResponse = postService.findList(pageable, projectId, userId)

    @PostMapping("/task")
    fun createTask(@RequestBody @Valid taskRequest:TaskRequest): PostCreatedResponse {
        val taskNumber = sequenceCreator.create("taskSeq")
        val task = taskRequest.convertToPostEntity(taskNumber)
        val postId = postService.create(task).toString()

        return PostCreatedResponse(postId, taskNumber)
    }

    @PutMapping("/task/status")
    fun changeTaskStatus(@RequestBody @Valid taskStatusRequest: TaskStatusRequest): ResponseEntity<String> {
        postService.changeTaskContents(taskStatusRequest)

        return ResponseEntity("success", HttpStatus.OK)
    }

    @PutMapping("/task/progress")
    fun changeTaskProgress(@RequestBody @Valid taskProgressRequest: TaskProgressRequest): ResponseEntity<String> {
        postService.changeTaskContents(taskProgressRequest)

        return ResponseEntity("success", HttpStatus.OK)
    }

    @PutMapping("/task/date")
    fun changeTaskDate(@RequestBody @Valid taskDateRequest: TaskDateRequest): ResponseEntity<String> {
        postService.changeTaskContents(taskDateRequest)

        return ResponseEntity("success", HttpStatus.OK)
    }
}