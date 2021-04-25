package com.example.certificationboard.post.application.request

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TaskModifyRequestTest{

    @Test
    fun test(){
        val taskModifyRequest: TaskModifyRequest = TaskStatusRequest("123123", "123123")

        assertEquals("123123", taskModifyRequest.postId)
    }
}