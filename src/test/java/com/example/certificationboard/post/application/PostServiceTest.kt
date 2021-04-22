package com.example.certificationboard.post.application

import com.example.certificationboard.member.domain.Member
import com.example.certificationboard.member.domain.MemberRepository
import com.example.certificationboard.post.domain.Post
import com.example.certificationboard.post.domain.TaskContents
import com.example.certificationboard.project.application.ProjectService
import com.example.certificationboard.project.domain.Project
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Transactional
internal open class PostServiceTest{

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var projectService: ProjectService

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
        )

        val taskContents1 = TaskContents(
                "test"
                , TaskContents.Status.REQUEST
                , LocalDateTime.of(2021, 4, 10, 0, 0)
                , LocalDateTime.of(2021, 4, 11, 23, 59)
                , listOf()
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

        assertFalse(findAll.isHasNext)
        assertEquals(2, findAll.postInfos.size)
    }
}