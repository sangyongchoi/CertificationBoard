package com.example.certificationboard.like.application

import com.example.certificationboard.common.exception.AlreadyRegisteredException
import com.example.certificationboard.like.domain.Like
import com.example.certificationboard.like.domain.LikeRepository
import com.example.certificationboard.projectparticipants.application.ProjectParticipantsService
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class LikeService(
    private val likeRepository: LikeRepository,
    private val projectParticipantsService: ProjectParticipantsService
) {

    fun create(like: Like, projectId: Long): Like {
        projectParticipantsService.validateParticipants(projectId, like.userId)

        val findByPostIdAndUserId = likeRepository.findByPostIdAndUserId(like.postId, like.userId)
        if(findByPostIdAndUserId != null){
            throw AlreadyRegisteredException("이미 좋아요를 누른 게시글입니다.")
        }

        return likeRepository.save(like)
    }

    fun delete(likeRequest: LikeRequest) {
        val projectId = likeRequest.projectId
        val postId = likeRequest.postId
        val userId = likeRequest.userId

        projectParticipantsService.validateParticipants(projectId, userId)

        likeRepository.deleteByPostIdAndUserId(ObjectId(postId), userId)
    }
}