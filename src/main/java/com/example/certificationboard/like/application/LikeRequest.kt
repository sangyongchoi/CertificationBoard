package com.example.certificationboard.like.application

import com.example.certificationboard.like.domain.Like
import org.bson.types.ObjectId
import javax.validation.constraints.NotEmpty

data class LikeRequest(
    val projectId: Long,
    @field:NotEmpty val postId: String,
    @field:NotEmpty val userId: String
) {

    fun convertToLikeEntity(): Like = Like(ObjectId(postId), userId)
}