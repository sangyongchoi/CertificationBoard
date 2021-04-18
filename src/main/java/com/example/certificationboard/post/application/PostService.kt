package com.example.certificationboard.post.application

import com.example.certificationboard.common.util.PageUtil
import com.example.certificationboard.post.domain.Post
import com.example.certificationboard.post.domain.PostRepository
import com.example.certificationboard.project.application.ProjectService
import org.bson.types.ObjectId
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class PostService(private val projectService: ProjectService, private val postRepository: PostRepository) {

    fun create(post: Post): ObjectId = postRepository.save(post).id

    fun findList(pageable:Pageable, userId: String, projectId: Long): PostResponse{
        val project = projectService.findById(projectId)
        val postPageInfo = postRepository.findAllByProjectId(pageable, projectId)
        val hasNext = PageUtil.hasNext(postPageInfo, pageable)
        val postList = postPageInfo.content.stream()
                .map { PostInfo(it.id, it.projectId, it.type, it.contents) }
                .collect(Collectors.toList())

        return PostResponse(hasNext, postList)
    }
}