package com.example.certificationboard.post.presentation

import com.example.certificationboard.common.sequence.SequenceCreator
import com.example.certificationboard.post.application.PostService
import com.example.certificationboard.post.application.request.*
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
    fun createTask(@RequestBody @Valid taskCreateRequest:TaskCreateRequest): PostCreatedResponse {
        val taskNumber = sequenceCreator.create("taskSeq")
        val task = taskCreateRequest.convertToPostEntity(taskNumber)
        val postId = postService.create(task).toString()

        return PostCreatedResponse(postId, taskNumber)
    }

    @PutMapping("/task")
    fun modifyTask(@RequestBody @Valid taskModifyRequest: TaskModifyRequest): ResponseEntity<String> {
        postService.changeTaskContents(taskModifyRequest)

        return ResponseEntity("success", HttpStatus.OK)
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

    @DeleteMapping("/post")
    fun delete(@RequestBody @Valid postDeleteRequest: PostDeleteRequest): ResponseEntity<String> {
        postService.delete(postDeleteRequest.postId, postDeleteRequest.userId)

        return ResponseEntity("success", HttpStatus.OK)
    }
}