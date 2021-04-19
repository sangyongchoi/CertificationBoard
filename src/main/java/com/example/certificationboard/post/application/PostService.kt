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
        isProjectParticipants(post.projectId, post.memberId)

        return postRepository.save(post).id
    }

    fun findList(pageable:Pageable, projectId: Long, userId: String): PostResponse {
        isProjectParticipants(projectId, userId)

        val postPageInfo = postRepository.findAllByProjectId(pageable, projectId)
        val hasNext = PageUtil.hasNext(postPageInfo, pageable)
        val postList = postPageInfo.content.stream()
                .map { PostInfo(it.id, it.projectId, it.type, it.contents) }
                .collect(Collectors.toList())

        return PostResponse(hasNext, postList)
    }

    private fun isProjectParticipants(projectId: Long, userId: String) {
        projectParticipantsService.isProjectParticipants(projectId, userId)
    }

}