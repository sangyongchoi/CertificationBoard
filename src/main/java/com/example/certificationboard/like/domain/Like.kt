package com.example.certificationboard.like.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Like(
    val postId: ObjectId,
    val userId: String
) {
    @Id
    private var id: ObjectId? = null

    override fun toString(): String {
        return "Like(postId=$postId, userId='$userId', id=$id)"
    }
}