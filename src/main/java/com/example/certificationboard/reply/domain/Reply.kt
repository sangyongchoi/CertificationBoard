package com.example.certificationboard.reply.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
class Reply(
    val postId: ObjectId,
    val writerId: String,
    val contents: String
) {
    @Id
    var id: ObjectId? = null
    @CreatedDate
    var createdAt: LocalDateTime? = null

    override fun toString(): String {
        return "Reply(postId=$postId, writerId='$writerId', contents='$contents', id=$id, createdAt=$createdAt)"
    }
}