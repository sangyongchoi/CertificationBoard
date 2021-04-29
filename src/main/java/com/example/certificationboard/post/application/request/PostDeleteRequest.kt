package com.example.certificationboard.post.application.request

import javax.validation.constraints.NotBlank

data class PostDeleteRequest(
    @field:NotBlank val postId: String,
    @field:NotBlank val userId: String
) {
}