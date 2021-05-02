package com.example.certificationboard.post.application

import com.example.certificationboard.member.domain.Member
import com.example.certificationboard.member.domain.MemberRepository
import com.example.certificationboard.post.application.request.TaskRequest
import com.example.certificationboard.post.application.response.ManagerInfo
import com.example.certificationboard.post.application.response.PostInfo
import com.example.certificationboard.post.application.response.PostListResponse
import com.example.certificationboard.post.application.response.TaskContentsDto
import com.example.certificationboard.post.domain.Contents
import com.example.certificationboard.post.domain.Post
import com.example.certificationboard.post.domain.PostRepository
import com.example.certificationboard.post.domain.TaskContents
import com.example.certificationboard.post.exception.UnauthorizedException
import com.example.certificationboard.projectparticipants.application.ProjectParticipantsService
import org.bson.types.ObjectId
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PostService(
        private val postRepository: PostRepository
        , private val projectParticipantsService: ProjectParticipantsService
) {

    fun create(post: Post): ObjectId {
        validateProjectParticipants(post.projectId, post.memberId)

        return postRepository.save(post).id
    }

    fun delete(postId: String, userId: String) {
        val post = findPostById(postId)

        if (post.memberId == userId) {
            postRepository.deleteById(postId)
        } else {
            throw UnauthorizedException("권한이 존재하지 않습니다.")
        }
    }

    fun findList(pageable:Pageable, projectId: Long, userId: String): PostListResponse {
        validateProjectParticipants(projectId, userId)


        val postPageInfo = postRepository.findAllByProjectIdOrderByIdDesc(pageable, projectId)
        val content = postPageInfo.content
        val managersInfo = getManagersInfo(content)
        val postList = content.map { PostInfo(it.id, it.projectId,it.memberId, managersInfo.getValue(it.memberId), it.type, getContents(it.contents, managersInfo)) }

        return PostListResponse(!postPageInfo.isLast, postList)
    }

    private fun validateProjectParticipants(projectId: Long, userId: String) {
        projectParticipantsService.validateParticipants(projectId, userId)
    }

    private fun getManagersInfo(postList: List<Post>): Map<String, String>{
        val managers = getManagers(postList)

        return managers.map { it.id to it.name }.toMap()
    }

    private fun getManagers(postList: List<Post>): List<Member>{
        val managersId = postList.filter { it.contents is TaskContents }
                .flatMap { (it.contents as TaskContents).managers }
                .toSet()

        val writersId = postList.map { it.memberId }
            .toSet()

        val usersId = managersId.plus(writersId)

        return projectParticipantsService.getManagersInfo(usersId)
    }

    private fun getContents(contents: Contents, managersInfo: Map<String, String>): Contents{
        return when (contents) {
            is TaskContents -> {
                val managersInfos = contents.managers.map { ManagerInfo(it, managersInfo.getValue(it)) }
                TaskContentsDto(contents, managersInfos)
            }
            else -> contents
        }
    }

    fun changeTaskContents(taskStatusRequest: TaskRequest): ObjectId {
        val post = findPostById(taskStatusRequest.postId)

        val taskContents = taskStatusRequest.convertToTaskContents(post.contents)
        post.changeTaskContents(taskContents)

        return postRepository.save(post).id
    }

    private fun findPostById(postId: String) = postRepository.findById(postId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 게시물입니다.") }

}