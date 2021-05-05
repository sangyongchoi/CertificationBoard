package com.example.certificationboard.reply.domain

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface ReplyRepository: MongoRepository<Reply, ObjectId> {
}