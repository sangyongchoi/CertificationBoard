package com.example.certificationboard.post.application

import com.example.certificationboard.like.application.LikeInfo
import com.example.certificationboard.member.domain.Member
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
import com.example.certificationboard.post.query.PostCustomRepository
import com.example.certificationboard.projectparticipants.application.ProjectParticipantsService
import com.example.certificationboard.reply.application.ReplyInfo
import com.example.certificationboard.reply.domain.Reply
import org.bson.types.ObjectId
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PostService(
        private val postRepository: PostRepository
        , private val projectParticipantsService: ProjectParticipantsService
        , private val postCustomRepository: PostCustomRepository
) {

    fun create(post: Post): ObjectId {
        validateProjectParticipants(post.projectId, post.memberId)

        return postRepository.save(post).id
    }

    fun delete(postId: String, userId: String) {
        val post = findPostById(postId)

        if (!post.memberId.equals(userId)) {
            throw UnauthorizedException("권한이 존재하지 않습니다.")
        }

        postRepository.deleteById(postId)
    }

    fun findList(pageable:Pageable, projectId: Long, userId: String): PostListResponse {
        validateProjectParticipants(projectId, userId)

        val postPageInfo = postCustomRepository.findByProjectId(projectId, pageable)
        val content = postPageInfo.content
        val usersInfo = getUsersInfo(content)
        val postList = content.map {
            PostInfo(
                it.id,
                it.projectId,
                it.memberId,
                usersInfo.getValue(it.memberId),
                it.type,
                getContents(it.contents, usersInfo),
                it.createdAt,
                it.likes.map { like -> LikeInfo(like.userId) },
                getReplies(it.replies, usersInfo)
            ) }

        return PostListResponse(!postPageInfo.isLast, postList)
    }

    private fun validateProjectParticipants(projectId: Long, userId: String) {
        projectParticipantsService.validateParticipants(projectId, userId)
    }

    private fun getUsersInfo(postList: List<Post>): Map<String, String>{
        val managers = getUsers(postList)

        return managers.map { it.id to it.name }.toMap()
    }

    private fun getUsers(postList: List<Post>): List<Member>{
        val managersId = postList.filter { it.contents is TaskContents }
                .flatMap { (it.contents as TaskContents).managers }
                .toSet()

        val writersId = postList.map { it.memberId }
            .toSet()

        val replyWriters = postList.flatMap { post ->
            post.replies.map { it.writerId }
        }

        val usersId = managersId.plus(writersId).plus(replyWriters)

        return projectParticipantsService.getUsersInfo(usersId)
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

    private fun getReplies(replies: List<Reply>, managersInfo: Map<String, String>): List<ReplyInfo> {
        return replies.map { ReplyInfo(it.id.toString(), it.writerId, managersInfo.getValue(it.writerId), it.contents, it.createdAt!!) }
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