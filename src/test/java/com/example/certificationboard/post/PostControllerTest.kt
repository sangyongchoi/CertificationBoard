package com.example.certificationboard.post

import com.example.certificationboard.ControllerTest
import com.example.certificationboard.post.application.response.PostInfo
import com.example.certificationboard.post.application.PostService
import com.example.certificationboard.post.application.response.PostResponse
import com.example.certificationboard.post.domain.Post
import com.example.certificationboard.post.domain.PostRepository
import com.example.certificationboard.post.domain.TaskContents
import com.example.certificationboard.project.application.ProjectService
import com.example.certificationboard.security.filter.JWTFilter
import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime


@WebMvcTest(controllers = [PostController::class])
@MockBean(JpaMetamodelMappingContext::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PostControllerTest: ControllerTest(){

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var postService: PostService

    @MockBean
    lateinit var projectService: ProjectService

    @MockBean
    lateinit var postRepository: PostRepository

    @BeforeAll
    fun whenSetup(){
        `when`(postService.findList(any(), anyLong(), anyString())).thenReturn(getDummyResponse())
    }

    private fun getDummyResponse(): PostResponse {
        val hasNext = false
        val postInfos = mutableListOf<PostInfo>()
        val managers = mutableListOf<String>()
        val taskContents = TaskContents("test",
                TaskContents.Status.REQUEST
                , LocalDateTime.of(2021, 4, 10, 0, 0)
                , LocalDateTime.of(2021, 4, 11, 23, 59)
                , managers
        )

        postInfos.add(PostInfo(ObjectId.get(), 1L, Post.Type.TASK, taskContents))
        postInfos.add(PostInfo(ObjectId.get(), 1L, Post.Type.TASK, taskContents))
        postInfos.add(PostInfo(ObjectId.get(), 1L, Post.Type.TASK, taskContents))

        return PostResponse(hasNext, postInfos)
    }

    @Test
    @DisplayName("토큰 누락 테스트")
    fun jwt_omission_test(){
        mockMvc
                .perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "20")
                )
                .andDo(print())
                .andExpect(status().isUnauthorized)
    }

    @Test
    @DisplayName("게시글 가져오기")
    fun find_post_list(){
        val userId = "csytest1"

        mockMvc
                .perform(get("/posts")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("userId", userId)
                        .param("projectId", "1")
                        .param("page", "0")
                        .param("size", "20")
                )
                .andDo(print())
                .andExpect(status().isOk)
    }

    @Test
    @DisplayName("업무 등록하기")
    fun create_task(){
        val testTaskRequest = TestTaskRequest("test", "REQUEST","2021-04-10T00:00:00", "2021-04-10T00:00:00", mutableListOf(), "csytest1", 1L)

        mockMvc
                .perform(post("/task")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTaskRequest))
                )
                .andDo(print())
                .andExpect(status().isOk)
    }

    @Test
    @DisplayName("업무 등록하기 - 유저 ID 누락")
    fun create_task_omission_userid(){
        val testTaskRequest = TestTaskRequest("test", "REQUEST","2021-04-10T00:00:00", "2021-04-10T00:00:00", mutableListOf(), "", 1L)

        mockMvc
                .perform(post("/task")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTaskRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("업무 등록하기 - 제목 누락")
    fun create_task_omission_title(){
        val testTaskRequest = TestTaskRequest("", "REQUEST","2021-04-10T00:00:00", "2021-04-10T00:00:00", mutableListOf(), "csytest1", 1L)

        mockMvc
                .perform(post("/task")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTaskRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("업무 등록하기 - 프로젝트ID 누락")
    fun create_task_omission_projectid(){
        val testTaskRequest = TestTaskRequest("test", "REQUEST","2021-04-10T00:00:00", "2021-04-10T00:00:00", mutableListOf(), "csytest1", null)

        mockMvc
                .perform(post("/task")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTaskRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("업무 등록하기 - 업무 상태 누락")
    fun create_task_omission_task_status(){
        val testTaskRequest = TestTaskRequest("test", "","2021-04-10T00:00:00", "2021-04-10T00:00:00", mutableListOf(), "csytest1", 1L)

        mockMvc
                .perform(post("/task")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTaskRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }
}

data class TestTaskRequest(
        val title: String
        , val taskStatus: String
        , val startDate: String
        , val endDate: String
        , val managers: List<String>
        , val userId: String
        , val projectId: Long?
) {
}