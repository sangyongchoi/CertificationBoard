package com.example.certificationboard.common.sequence

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class SequenceCreatorTest {

    @Autowired
    lateinit var sequenceCreator: SequenceCreator

    @Test
    @DisplayName("시퀀스 생성 테스트")
    fun seq_create_test(){
        val seq = sequenceCreator.create("taskSeq")

        println(seq)
    }
}