package com.example.certificationboard.reply.application

import com.example.certificationboard.common.exception.NotAllowedFunctionException
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
import com.example.certificationboard.reply.domain.Reply
import com.example.certificationboard.reply.domain.ReplyRepository
import org.bson.types.ObjectId
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
    lateinit var replyId: String

    @BeforeAll
    fun setup(){
        val member = Member(userId, "csytest1", "test", false)
        val savedMember = memberRepository.save(member)
        val member2 = Member("failtest1", "csytest1", "test", false)
        val member3 = Member("failtest2", "csytest1", "test", false)
        memberRepository.save(member2)
        memberRepository.save(member3)
        val project = Project(savedMember.organizationId, savedMember.id, "test", "test")
        val createdProject = projectService.create(project)

        createdProjectId = createdProject.id

        insertParticipants(createdProject, member)
        insertParticipants(createdProject, member3)

        insertTask(project, savedMember)
        insertReply()
    }

    private fun insertParticipants(project: Project, member: Member) {
        val projectParticipantsId = ProjectParticipantsId(project, member)

        projectParticipantsService.join(projectParticipantsId, ProjectParticipants.Role.ADMIN)
    }

    private fun insertTask(project: Project, savedMember: Member) {
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

    private fun insertReply() {
        val reply = replyRepository.save(Reply(ObjectId(savedPostId), userId, "test"))
        replyId = reply.id.toString()
    }

    @AfterAll
    fun after(){
        replyRepository.deleteAll()
    }

    @Test
    @DisplayName("댓글 등록 테스트")
    @Order(1)
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
    @Order(2)
    fun write_fail() {
        assertThrows<NotParticipantsException> {
            var replyRequest = ReplyRequest(createdProjectId!!, savedPostId, "failtest1", "contents")
            val reply = replyService.write(replyRequest)
        }
    }

    @Test
    @DisplayName("댓글 수정")
    @Order(3)
    fun modify() {
        // given
        val request = ReplyModifyRequest(createdProjectId!!, replyId, userId, "modify")

        // when
        replyService.modify(request)

        // then
        // success
    }

    @Test
    @DisplayName("댓글 수정 - 참여자가 아닐때")
    @Order(4)
    fun modify_fail_when_not_participants() {
        assertThrows<NotParticipantsException> {
            val request = ReplyModifyRequest(createdProjectId!!, replyId, "failtest1", "modify")
            replyService.modify(request)
        }
    }

    @Test
    @DisplayName("댓글 수정 - 작성자가 아닐때")
    @Order(5)
    fun modify_fail_when_not_writer() {
        assertThrows<NotAllowedFunctionException> {
            val request = ReplyModifyRequest(createdProjectId!!, replyId, "failtest2", "modify")
            replyService.modify(request)
        }
    }

    @Test
    @DisplayName("댓글 삭제 - 작성자가 아닐 때")
    @Order(6)
    fun delete_fail_when_not_writer() {
        assertThrows<NotAllowedFunctionException> {
            val request = ReplyDeleteRequest(createdProjectId!!, replyId, "failtest2")
            replyService.delete(request)
        }
    }

    @Test
    @DisplayName("댓글 삭제 - 참여자가 아닐 때")
    @Order(7)
    fun delete_fail_when_not_participants() {
        assertThrows<NotParticipantsException> {
            val request = ReplyDeleteRequest(createdProjectId!!, replyId, "failtest1")
            replyService.delete(request)
        }
    }

    @Test
    @DisplayName("댓글 삭제")
    @Order(8)
    fun delete() {
        val request = ReplyDeleteRequest(createdProjectId!!, replyId, userId)
        replyService.delete(request)
        // success
    }
}