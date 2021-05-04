package com.example.certificationboard

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import kotlin.test.assertNotNull

@SpringBootTest
class MongoDbTest {

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Test
    fun test() {
        assertNotNull(mongoTemplate)
    }
}