package com.example.certificationboard.post.application

import com.example.certificationboard.common.util.PageUtil
import com.example.certificationboard.post.application.response.PostResponse
import com.example.certificationboard.post.domain.Post
import com.example.certificationboard.post.domain.PostRepository
import com.example.certificationboard.project.application.ProjectParticipantsService
import org.bson.types.ObjectId
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class PostService(
        private val postRepository: PostRepository
        , private val projectParticipantsService: ProjectParticipantsService
) {

    fun create(post: Post): ObjectId {
        val isProjectParticipants = projectParticipantsService.isProjectParticipants(post.projectId, post.memberId)

        if(!isProjectParticipants){
            throw IllegalArgumentException("잘못된 인자입니다")
        }

        return postRepository.save(post).id
    }

    fun findList(pageable:Pageable, projectId: Long, userId: String): PostResponse {
        val isProjectParticipants = projectParticipantsService.isProjectParticipants(projectId, userId)

        if(!isProjectParticipants){
            throw IllegalArgumentException("잘못된 인자입니다")
        }

        val postPageInfo = postRepository.findAllByProjectId(pageable, projectId)
        val hasNext = PageUtil.hasNext(postPageInfo, pageable)
        val postList = postPageInfo.content.stream()
                .map { PostInfo(it.id, it.projectId, it.type, it.contents) }
                .collect(Collectors.toList())

        return PostResponse(hasNext, postList)
    }

}