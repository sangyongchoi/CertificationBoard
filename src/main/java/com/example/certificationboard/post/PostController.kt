package com.example.certificationboard.post

import com.example.certificationboard.post.application.PostResponse
import com.example.certificationboard.post.application.PostService
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PostController(private val postService: PostService) {

    @GetMapping("/posts")
    fun list(pageable: Pageable, userId:String, projectId: Long): PostResponse {
        return postService.findList(pageable, userId, projectId)
    }
}