package com.example.certificationboard.common.sequence

import org.springframework.data.mongodb.core.FindAndModifyOptions.options
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update

import org.springframework.stereotype.Component


@Component
class SequenceCreator(
    private var mongoTemplate: MongoTemplate
) {

    fun create(sequenceName: String): Long {
        val counter: DatabaseSequence = mongoTemplate.findAndModify(
            query(where("_id").`is`(sequenceName)),
            Update().inc("seq", 1), options().returnNew(true).upsert(true),
            DatabaseSequence::class.java
        )

        return counter.seq
    }
}