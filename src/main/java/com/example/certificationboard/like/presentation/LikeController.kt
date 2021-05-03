package com.example.certificationboard.like.presentation

import com.example.certificationboard.like.application.LikeRequest
import com.example.certificationboard.like.application.LikeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class LikeController(
    private val likeService: LikeService
) {

    @PostMapping("/post/like")
    fun create(@RequestBody @Valid likeRequest: LikeRequest): ResponseEntity<String>{
        val like = likeRequest.convertToLikeEntity()
        val projectId = likeRequest.projectId

        likeService.create(like, projectId)

        return ResponseEntity.ok("success")
    }

    @DeleteMapping("/post/like")
    fun delete(@RequestBody @Valid likeRequest: LikeRequest): ResponseEntity<String>{
        likeService.delete(likeRequest)

        return ResponseEntity.ok("success")
    }
}