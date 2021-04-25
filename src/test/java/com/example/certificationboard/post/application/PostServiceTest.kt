package com.example.certificationboard.post.application

import com.example.certificationboard.member.domain.Member
import com.example.certificationboard.member.domain.MemberRepository
import com.example.certificationboard.post.application.request.TaskDateRequest
import com.example.certificationboard.post.application.request.TaskProgressRequest
import com.example.certificationboard.post.application.request.TaskStatusRequest
import com.example.certificationboard.post.domain.Post
import com.example.certificationboard.post.domain.PostRepository
import com.example.certificationboard.post.domain.TaskContents
import com.example.certificationboard.project.application.ProjectService
import com.example.certificationboard.project.domain.Project
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal open class PostServiceTest{

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var projectService: ProjectService

    @Autowired
    lateinit var postRepository: PostRepository

    @Autowired
    lateinit var postService: PostService

    private lateinit var savedProject: Project

    val userId = "csytest1"

    @BeforeAll
    fun setup(){
        val member = Member(userId, "csytest1", "test", false)
        val savedMember = memberRepository.save(member)
        val project = Project(savedMember.organizationId, savedMember.id, "test", "test")
        projectService.create(project, member)
        val taskContents = TaskContents(
                "test"
                , TaskContents.Status.REQUEST
                , LocalDateTime.of(2021, 4, 10, 0, 0)
                , LocalDateTime.of(2021, 4, 11, 23, 59)
                , listOf("csytest1")
                , TaskContents.Priority.NORMAL
                , 0
        )

        val taskContents1 = TaskContents(
                "test"
                , TaskContents.Status.REQUEST
                , LocalDateTime.of(2021, 4, 10, 0, 0)
                , LocalDateTime.of(2021, 4, 11, 23, 59)
                , listOf()
                , TaskContents.Priority.EMERGENCY
                , 20
        )

        val post = Post(project.id
                ,savedMember.id
                ,Post.Type.TASK
                ,taskContents)

        val post2 = Post(project.id
                ,savedMember.id
                ,Post.Type.TASK
                ,taskContents1)

        savedProject = project

        postService.create(post)
        postService.create(post2)
    }

    @Test
    @DisplayName("조회 테스트")
    fun find_post_list(){
        val pageable = PageRequest.of(0, 20)
        val findAll = postService.findList(pageable, savedProject.id, userId)

        findAll.postInfos.forEach{ print(it)}

        assertFalse(findAll.isHasNext)
        //assertEquals(2, findAll.postInfos.size)
    }
    
    @Test
    @DisplayName("업무상태 변경 테스트")
    fun change_task_status(){
        // given
        val postId = postRepository.findAll()[0]?.id.toString()
        val status = "REQUEST"
        val taskStatusRequest = TaskStatusRequest(postId, status)
        // then
        val changedPostId = postService.changeTaskContents(taskStatusRequest);

        // when
        val contents = postRepository.findById(changedPostId.toString()).get().contents

        assertEquals(TaskContents.Status.REQUEST, when (contents) {
            is TaskContents -> contents.taskStatus
            else -> null
        })
    }

    @Test
    @DisplayName("업무 진척도 변경 테스트")
    fun change_task_progress(){
        // given
        val postId = postRepository.findAll()[0]?.id.toString()
        val progress = "40"
        val taskStatusRequest = TaskProgressRequest(postId, progress)
        // then
        val changedPostId = postService.changeTaskContents(taskStatusRequest);

        // when
        val contents = postRepository.findById(changedPostId.toString()).get().contents

        assertEquals(40, when (contents) {
            is TaskContents -> contents.progress
            else -> null
        })
    }

    @Test
    @DisplayName("업무 날짜 변경 테스트")
    fun change_task_date(){
        // given
        val postId = postRepository.findAll()[0]?.id.toString()
        val startDate = LocalDateTime.parse("2021-04-10T00:00:00")
        val endDate = LocalDateTime.parse("2021-04-12T00:00:00")
        val taskStatusRequest = TaskDateRequest(postId, startDate, endDate)
        // then
        val changedPostId = postService.changeTaskContents(taskStatusRequest);

        // when
        val contents = postRepository.findById(changedPostId.toString()).get().contents

        assertEquals(endDate, when (contents) {
            is TaskContents -> contents.endDate
            else -> null
        })
    }

}