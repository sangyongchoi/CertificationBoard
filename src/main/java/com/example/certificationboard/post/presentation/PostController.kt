package com.example.certificationboard.post.presentation

import com.example.certificationboard.post.application.PostService
import com.example.certificationboard.post.application.request.TaskRequest
import com.example.certificationboard.post.application.request.TaskStatusRequest
import com.example.certificationboard.post.application.response.PostResponse
import org.bson.types.ObjectId
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class PostController(
        private val postService: PostService
) {

    @GetMapping("/posts/{projectId}")
    fun list(pageable: Pageable, userId:String,@PathVariable projectId: Long): PostResponse = postService.findList(pageable, projectId, userId)

    @PostMapping("/task")
    fun createTask(@RequestBody @Valid taskRequest:TaskRequest): ObjectId {
        val task = taskRequest.convertToPostEntity()
        return postService.create(task)
    }

    @PutMapping("/task/status")
    fun changeTaskStatus(@RequestBody @Valid taskStatusRequest: TaskStatusRequest): ResponseEntity<String> {
        postService.changeTaskContents(taskStatusRequest)

        return ResponseEntity("success", HttpStatus.OK)
    }
}