package com.example.certificationboard.reply.presentation

import com.example.certificationboard.reply.application.ReplyRequest
import com.example.certificationboard.reply.application.ReplyResponse
import com.example.certificationboard.reply.application.ReplyService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class ReplyController(
    private val replyService: ReplyService
) {

    @PostMapping("/post/reply")
    fun write(@RequestBody @Valid replyRequest: ReplyRequest): ReplyResponse {
        val reply = replyService.write(replyRequest)
        return ReplyResponse(reply.id.toString(), reply.createdAt!!)
    }
}