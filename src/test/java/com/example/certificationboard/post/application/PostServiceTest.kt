package com.example.certificationboard.post.application

import com.example.certificationboard.member.domain.Member
import com.example.certificationboard.member.domain.MemberRepository
import com.example.certificationboard.post.application.request.*
import com.example.certificationboard.post.domain.Post
import com.example.certificationboard.post.domain.PostRepository
import com.example.certificationboard.post.domain.TaskContents
import com.example.certificationboard.post.exception.UnauthorizedException
import com.example.certificationboard.project.application.ProjectService
import com.example.certificationboard.project.domain.Project
import com.example.certificationboard.projectparticipants.application.ProjectParticipantsService
import com.example.certificationboard.projectparticipants.domain.ProjectParticipants
import com.example.certificationboard.projectparticipants.domain.ProjectParticipantsId
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal open class PostServiceTest{

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var projectService: ProjectService

    @Autowired
    lateinit var postRepository: PostRepository

    @Autowired
    lateinit var postService: PostService

    @Autowired
    lateinit var projectParticipantsService: ProjectParticipantsService

    private lateinit var savedProject: Project

    val userId = "csytest1"

    private lateinit var savedPostId: String
    private lateinit var savedPostId2: String

    @BeforeAll
    fun setup(){
        val member = Member(userId, "csytest1", "test", false)
        val savedMember = memberRepository.save(member)
        val project = Project(savedMember.organizationId, savedMember.id, "test", "test")
        val createdProject = projectService.create(project)

        val projectParticipantsId = ProjectParticipantsId(createdProject, member)

        projectParticipantsService.join(projectParticipantsId, ProjectParticipants.Role.ADMIN)

        val taskContents = TaskContents(
            "test",
            TaskContents.Status.REQUEST,
            LocalDateTime.of(2021, 4, 10, 0, 0),
            LocalDateTime.of(2021, 4, 11, 23, 59),
            listOf("csytest1"),
            TaskContents.Priority.NORMAL,
            0,
            1
            ,"test"
        )

        val taskContents1 = TaskContents(
            "test",
            TaskContents.Status.REQUEST,
            LocalDateTime.of(2021, 4, 10, 0, 0),
            LocalDateTime.of(2021, 4, 11, 23, 59),
            listOf(),
            TaskContents.Priority.EMERGENCY,
            20,
            2
            ,"test"
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

        savedPostId = postService.create(post).toString()
        savedPostId2 = postService.create(post2).toString()
    }

    @AfterAll
    fun after(){
        // ????????? ??????
        postRepository.deleteById(savedPostId)
        postRepository.deleteById(savedPostId2)
    }

    @Test
    @DisplayName("?????? ?????????")
    @Order(1)
    fun find_post_list(){
        val pageable = PageRequest.of(0, 20)
        val findAll = postService.findList(pageable, savedProject.id, userId)

        findAll.postInfos.forEach{ print(it)}

        assertFalse(findAll.isHasNext)
        //assertEquals(2, findAll.postInfos.size)
    }
    
    @Test
    @DisplayName("???????????? ?????? ?????????")
    @Order(2)
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
    @DisplayName("?????? ????????? ?????? ?????????")
    @Order(3)
    fun change_task_progress(){
        // given
        val postId = postRepository.findAll()[0]?.id.toString()
        val progress = 40
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
    @DisplayName("?????? ?????? ?????? ?????????")
    @Order(4)
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

    @Test
    @DisplayName("?????? ?????? ?????????")
    @Order(5)
    fun change_task(){
        // given
        val postId = postRepository.findAll()[0]?.id.toString()
        val startDate = LocalDateTime.parse("2021-04-10T00:00:00")
        val endDate = LocalDateTime.parse("2021-04-12T00:00:00")
        val taskModifyRequest = TaskModifyRequest(
            postId,
            "modify",
            "GOING",
            startDate,
            endDate,
            null,
            60,
            "USUALLY",
            "modify"
        )

        // then
        val changedPostId = postService.changeTaskContents(taskModifyRequest);

        // when
        val contents = postRepository.findById(changedPostId.toString()).get().contents
        val taskContents = contents as TaskContents

        assertEquals("modify", taskContents.title)
        assertEquals("modify", taskContents.context)
        assertEquals(TaskContents.Status.GOING, taskContents.taskStatus)
        assertEquals(TaskContents.Priority.USUALLY, taskContents.priority)
        assertEquals(60, taskContents.progress)
    }

    @Test
    @DisplayName("????????? ?????? ?????????")
    @Order(6)
    fun change_managers(){
        // given
        val postId = postRepository.findAll()[0]?.id.toString()
        val taskModifyRequest = TaskManagerRequest(
            postId,
            null
        )

        // then
        val changedPostId = postService.changeTaskContents(taskModifyRequest);

        // when
        val contents = postRepository.findById(changedPostId.toString()).get().contents
        val taskContents = contents as TaskContents

        assertEquals(0, taskContents.managers.size)
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    @Order(7)
    fun post_delete_fail(){
        assertThrows<UnauthorizedException> {
            // given
            var postId = savedPostId
            var userId = "csytest2"

            // when
            postService.delete(postId, userId)
        }
    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    @Order(8)
    fun post_delete_success(){
        // given
        var postId = savedPostId
        var userId = "csytest1"

        // when
        postService.delete(postId, userId)

        // then
        // success
    }
}