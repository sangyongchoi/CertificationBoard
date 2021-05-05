package com.example.certificationboard.reply.domain

import com.example.certificationboard.common.exception.NotAllowedFunctionException
import com.example.certificationboard.reply.application.ReplyModifyRequest
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
class Reply(
    val postId: ObjectId,
    val writerId: String,
    var contents: String
) {
    @Id
    var id: ObjectId? = null
    @CreatedDate
    var createdAt: LocalDateTime? = null

    fun changeContents(modifyRequest: ReplyModifyRequest) {
        val userId = modifyRequest.userId
        val modifyContents = modifyRequest.contents

        if (!isWriter(userId)) {
            throw NotAllowedFunctionException("작성자만 수정할 수 있습니다.")
        }

        this.contents = modifyContents
    }

    fun isWriter(userId: String) = writerId == userId

    override fun toString(): String {
        return "Reply(postId=$postId, writerId='$writerId', contents='$contents', id=$id, createdAt=$createdAt)"
    }
}