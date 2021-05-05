package com.example.certificationboard.reply.presentation

import com.example.certificationboard.reply.application.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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

    @PutMapping("/post/reply")
    fun modify(@RequestBody @Valid modifyRequest: ReplyModifyRequest): ResponseEntity<String> {
        replyService.modify(modifyRequest)

        return ResponseEntity.ok("success")
    }

    @DeleteMapping("/post/reply")
    fun delete(@RequestBody @Valid deleteRequest: ReplyDeleteRequest): ResponseEntity<String>? {
        replyService.delete(deleteRequest)

        return ResponseEntity.ok("success")
    }
}