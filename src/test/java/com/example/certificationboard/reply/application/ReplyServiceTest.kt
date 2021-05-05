package com.example.certificationboard.reply.application

import com.example.certificationboard.member.domain.Member
import com.example.certificationboard.member.domain.MemberRepository
import com.example.certificationboard.post.application.PostService
import com.example.certificationboard.post.domain.Post
import com.example.certificationboard.post.domain.TaskContents
import com.example.certificationboard.project.application.ProjectService
import com.example.certificationboard.project.domain.Project
import com.example.certificationboard.projectparticipants.application.ProjectParticipantsService
import com.example.certificationboard.projectparticipants.domain.ProjectParticipants
import com.example.certificationboard.projectparticipants.domain.ProjectParticipantsId
import com.example.certificationboard.projectparticipants.exception.NotParticipantsException
import com.example.certificationboard.reply.domain.ReplyRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class ReplyServiceTest {

    @Autowired
    lateinit var replyRepository: ReplyRepository

    @Autowired
    lateinit var replyService: ReplyService

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var projectService: ProjectService

    @Autowired
    lateinit var projectParticipantsService: ProjectParticipantsService

    @Autowired
    lateinit var postService: PostService

    val userId = "replytest1"

    var createdProjectId: Long? = null
    lateinit var savedPostId: String

    @BeforeAll
    fun setup(){
        val member = Member(userId, "csytest1", "test", false)
        val savedMember = memberRepository.save(member)
        val member2 = Member("failtest1", "csytest1", "test", false)
        memberRepository.save(member2)
        val project = Project(savedMember.organizationId, savedMember.id, "test", "test")
        val createdProject = projectService.create(project)

        createdProjectId = createdProject.id

        val projectParticipantsId = ProjectParticipantsId(createdProject, member)
        val projectParticipants = ProjectParticipants(projectParticipantsId, ProjectParticipants.Role.ADMIN, false)

        projectParticipantsService.join(projectParticipants)

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

        val post = Post(project.id
            ,savedMember.id
            , Post.Type.TASK
            ,taskContents)


        savedPostId = postService.create(post).toString()
    }

    @AfterAll
    fun after(){
        replyRepository.deleteAll()
    }

    @Test
    @DisplayName("댓글 등록 테스트")
    fun write() {
        var replyRequest = ReplyRequest(createdProjectId!!, savedPostId, userId, "contents")

        val reply = replyService.write(replyRequest)

        println(reply)
        assertNotNull(reply)
        assertNotNull(reply.id)
        assertNotNull(reply.createdAt)
    }

    @Test
    @DisplayName("댓글 등록 실패 - 참여자가 아닐 때")
    fun write_fail() {
        assertThrows<NotParticipantsException> {
            var replyRequest = ReplyRequest(createdProjectId!!, savedPostId, "failtest1", "contents")
            val reply = replyService.write(replyRequest)
        }
    }
}