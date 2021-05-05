package com.example.certificationboard.reply.application

import com.example.certificationboard.common.exception.NotAllowedFunctionException
import com.example.certificationboard.projectparticipants.application.ProjectParticipantsService
import com.example.certificationboard.reply.domain.Reply
import com.example.certificationboard.reply.domain.ReplyRepository
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class ReplyService(
    private val replyRepository: ReplyRepository,
    private val projectParticipantsService: ProjectParticipantsService
) {

    fun write(createRequest: ReplyRequest): Reply {
        val projectId = createRequest.projectId
        val userId = createRequest.userId

        projectParticipantsService.validateParticipants(projectId, userId)

        val reply = createRequest.convertToReplyEntity()

        return replyRepository.save(reply)
    }

    fun modify(modifyRequest: ReplyModifyRequest) {
        val projectId = modifyRequest.projectId
        val userId = modifyRequest.userId

        projectParticipantsService.validateParticipants(projectId, userId)

        val replyId = modifyRequest.replyId

        val reply = replyRepository.findById(ObjectId(replyId))
            .orElseThrow{ throw IllegalArgumentException("존재하지 않는 댓글입니다.")}

        reply.changeContents(modifyRequest)
        replyRepository.save(reply)
    }

    fun delete(deleteRequest: ReplyDeleteRequest) {
        val projectId = deleteRequest.projectId
        val replyId = deleteRequest.replyId
        val requestId = deleteRequest.userId

        projectParticipantsService.validateParticipants(projectId, requestId)

        val reply = replyRepository.findById(ObjectId(replyId))
            .orElseThrow{ throw IllegalArgumentException("존재하지 않는 댓글입니다.")}

        val isWriter = reply.isWriter(requestId)

        if (!isWriter) {
            throw NotAllowedFunctionException("작성자만 수정할 수 있습니다.")
        }

        replyRepository.delete(reply)
    }
}