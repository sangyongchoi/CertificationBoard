package com.example.certificationboard.like.domain

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface LikeRepository : MongoRepository<Like, ObjectId> {
    fun findByPostIdAndUserId(postId: ObjectId, userId: String): Like?
    fun deleteByPostIdAndUserId(postId: ObjectId, userId: String)
}