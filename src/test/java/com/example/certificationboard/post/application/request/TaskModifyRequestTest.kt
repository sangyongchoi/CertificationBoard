package com.example.certificationboard.post.application.request

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class TaskModifyRequestTest{

    @Test
    fun test(){
        val of = setOf("1", "2", "3")
        val of1 = setOf("2", "3", "4")

        val plus = of.plus(of1)

        for (e in plus) {
            println(e)
        }
    }
}