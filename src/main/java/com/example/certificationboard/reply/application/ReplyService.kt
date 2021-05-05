package com.example.certificationboard.reply.application

import com.example.certificationboard.projectparticipants.application.ProjectParticipantsService
import com.example.certificationboard.reply.domain.Reply
import com.example.certificationboard.reply.domain.ReplyRepository
import org.springframework.stereotype.Service

@Service
class ReplyService(
    private val replyRepository: ReplyRepository,
    private val projectParticipantsService: ProjectParticipantsService
) {

    fun write(replyRequest: ReplyRequest): Reply {
        val projectId = replyRequest.projectId
        val userId = replyRequest.userId

        projectParticipantsService.validateParticipants(projectId, userId)

        val reply = replyRequest.convertToReplyEntity()

        return replyRepository.save(reply)
    }
}