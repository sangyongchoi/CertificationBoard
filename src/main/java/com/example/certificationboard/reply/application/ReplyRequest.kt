package com.example.certificationboard.reply.application

import com.example.certificationboard.reply.domain.Reply
import org.bson.types.ObjectId
import javax.validation.constraints.NotEmpty

data class ReplyRequest(
    val projectId: Long,
    @field:NotEmpty val postId: String,
    @field:NotEmpty val userId: String,
    @field:NotEmpty val contents: String
) {

    fun convertToReplyEntity() = Reply(ObjectId(postId), userId, contents)
}