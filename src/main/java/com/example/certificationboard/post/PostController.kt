package com.example.certificationboard.post

import com.example.certificationboard.post.application.PostService
import com.example.certificationboard.post.application.request.TaskRequest
import com.example.certificationboard.post.application.response.PostResponse
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class PostController(
        private val postService: PostService
) {

    @GetMapping("/posts")
    fun list(pageable: Pageable, userId:String, projectId: Long): PostResponse = postService.findList(pageable, projectId, userId)

    @PostMapping("/task")
    fun createTask(@RequestBody @Valid taskRequest:TaskRequest){
        val task = taskRequest.convertToPostEntity()
        postService.create(task)
    }
}