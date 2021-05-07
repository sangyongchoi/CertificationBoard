package com.example.certificationboard.like.application

import com.example.certificationboard.common.exception.AlreadyRegisteredException
import com.example.certificationboard.like.domain.Like
import com.example.certificationboard.like.domain.LikeRepository
import com.example.certificationboard.member.application.MemberService
import com.example.certificationboard.member.domain.Member
import com.example.certificationboard.post.application.PostService
import com.example.certificationboard.post.domain.Post
import com.example.certificationboard.post.domain.TaskContents
import com.example.certificationboard.project.application.ProjectService
import com.example.certificationboard.project.domain.Project
import com.example.certificationboard.projectparticipants.application.ProjectParticipantsService
import com.example.certificationboard.projectparticipants.domain.ProjectParticipants
import com.example.certificationboard.projectparticipants.domain.ProjectParticipantsId
import com.example.certificationboard.projectparticipants.exception.NotParticipantsException
import junit.framework.Assert.assertNotNull
import org.bson.types.ObjectId
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
internal class LikeServiceTest {

    @Autowired
    lateinit var likeService: LikeService

    @Autowired
    lateinit var memberService: MemberService

    @Autowired
    lateinit var projectService: ProjectService

    @Autowired
    lateinit var projectParticipantsService: ProjectParticipantsService

    @Autowired
    lateinit var postService: PostService

    @Autowired
    lateinit var likeRepository: LikeRepository

    lateinit var postId: ObjectId

    var projectId: Long = 0

    @BeforeAll
    fun setUp() {
        val member = Member("liketest1", "testtest", "test", false)
        val member2 = Member("liketest2", "testtest", "test", false)
        val signUp = memberService.signUp(member)
        memberService.signUp(member2)

        val project = Project(signUp.organizationId, "liketest1", "test", "test")
        val create = projectService.create(project)
        projectId = create.id

        val projectParticipantsId = ProjectParticipantsId(create, signUp)

        projectParticipantsService.join(projectParticipantsId, ProjectParticipants.Role.ADMIN)

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
            ,signUp.id
            , Post.Type.TASK
            ,taskContents1)

        postId = postService.create(post)
    }

    @AfterAll
    fun after(){
        postService.delete(postId.toString(), "liketest1")
        likeRepository.deleteAll()
    }

    @Test
    @DisplayName("좋아요 삭제")
    @Order(1)
    fun delete(){
        // given
        val like = Like(postId, "liketest1")
        val create = likeService.create(like, projectId)
        val likeRequest = LikeRequest(projectId, postId.toString(), "liketest1")
        println(create)
        // when
        likeService.delete(likeRequest)
    }

    @Test
    @DisplayName("좋아요 테스트")
    @Order(2)
    fun like() {
        val like = Like(postId, "liketest1")

        val create = likeService.create(like, projectId)

        assertNotNull(create)
    }

    @Test
    @DisplayName("좋아요 테스트 - 이미 존재할 때")
    @Order(3)
    fun like_already_registered() {
        assertThrows<AlreadyRegisteredException> {
            val like = Like(postId, "liketest1")

            likeService.create(like, projectId)
            likeService.create(like, projectId)
        }
    }

    @Test
    @DisplayName("좋아요 테스트 - 참여자가 아닌경우")
    @Order(4)
    fun like_not_participants() {
        assertThrows<NotParticipantsException> {
            val like = Like(postId, "liketest2")
            val create = likeService.create(like, projectId)
        }
    }
}