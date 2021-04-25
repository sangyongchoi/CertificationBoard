package com.example.certificationboard.post

import com.example.certificationboard.ControllerTest
import com.example.certificationboard.config.MockitoHelper
import com.example.certificationboard.post.application.response.PostInfo
import com.example.certificationboard.post.application.PostService
import com.example.certificationboard.post.application.request.TaskDateRequest
import com.example.certificationboard.post.application.request.TaskProgressRequest
import com.example.certificationboard.post.application.request.TaskStatusRequest
import com.example.certificationboard.post.application.response.PostListResponse
import com.example.certificationboard.post.domain.Post
import com.example.certificationboard.post.domain.PostRepository
import com.example.certificationboard.post.domain.TaskContents
import com.example.certificationboard.post.presentation.PostController
import com.example.certificationboard.project.application.ProjectService
import com.example.certificationboard.security.filter.JWTFilter
import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
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
        `when`(postService.findList(MockitoHelper.anyObject(), anyLong(), anyString())).thenReturn(getDummyResponse())
        `when`(postService.create(MockitoHelper.anyObject())).thenReturn(ObjectId())
    }

    private fun getDummyResponse(): PostListResponse {
        val hasNext = false
        val postInfos = mutableListOf<PostInfo>()
        val managers = mutableListOf<String>()
        val taskContents = TaskContents("test",
                TaskContents.Status.REQUEST
                , LocalDateTime.of(2021, 4, 10, 0, 0)
                , LocalDateTime.of(2021, 4, 11, 23, 59)
                , managers
                , TaskContents.Priority.NORMAL
                , 0
        )

        postInfos.add(PostInfo(ObjectId.get(), 1L, Post.Type.TASK, taskContents))
        postInfos.add(PostInfo(ObjectId.get(), 1L, Post.Type.TASK, taskContents))
        postInfos.add(PostInfo(ObjectId.get(), 1L, Post.Type.TASK, taskContents))

        return PostListResponse(hasNext, postInfos)
    }

    @Test
    @DisplayName("토큰 누락 테스트")
    fun jwt_omission_test(){
        mockMvc
                .perform(get("/posts/1")
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
                .perform(get("/posts/1")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("userId", userId)
                        .param("page", "0")
                        .param("size", "20")
                )
                .andDo(print())
                .andExpect(status().isOk)
    }

    @Test
    @DisplayName("업무 등록하기")
    fun create_task(){
        val testTaskRequest = TestTaskRequest("test", "REQUEST","2021-04-10T00:00:00", "2021-04-10T00:00:00", mutableListOf(),
                0, "NORMAL", "csytest1", 1L)

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
        val testTaskRequest = TestTaskRequest("test", "REQUEST","2021-04-10T00:00:00", "2021-04-10T00:00:00", mutableListOf(),
                0, "NORMAL","", 1L)

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
        val testTaskRequest = TestTaskRequest("", "REQUEST","2021-04-10T00:00:00", "2021-04-10T00:00:00", mutableListOf(),
                0, "NORMAL","csytest1", 1L)

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
        val testTaskRequest = TestTaskRequest("test", "REQUEST","2021-04-10T00:00:00", "2021-04-10T00:00:00", mutableListOf(),
                0, "NORMAL", "csytest1", null)

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
        val testTaskRequest = TestTaskRequest("test", "","2021-04-10T00:00:00",
                "2021-04-10T00:00:00", mutableListOf(),0, "NORMAL",
                "csytest1", 1L)

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
    @DisplayName("업무상태 변경 테스트 - jwt 누락")
    fun change_task_status_omission_jwt(){
        mockMvc
                .perform(put("/task/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized)
    }

    @Test
    @DisplayName("업무상태 변경 테스트 - postId 누락")
    fun change_task_status_omission_post_id(){
        val data = TaskStatusRequest("", "REQUEST")

        mockMvc
                .perform(put("/task/status")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data))
                )
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("업무상태 변경 테스트 - status 누락")
    fun change_task_status_omission_status(){
        val data = TaskStatusRequest("1231232133", "")

        mockMvc
                .perform(put("/task/status")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data))
                )
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("업무상태 변경 테스트")
    fun change_task_status_omission_success(){
        `when`(postService.changeTaskContents(MockitoHelper.anyObject())).thenReturn(ObjectId())
        val data = TaskStatusRequest("1231232133", "REQUEST")

        mockMvc
                .perform(put("/task/status")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data))
                )
                .andDo(print())
                .andExpect(status().isOk)
    }

    @Test
    @DisplayName("업무 진척도 변경 테스트 - jwt 누락")
    fun change_task_progress_omission_jwt(){
        mockMvc
                .perform(put("/task/progress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized)
    }

    @Test
    @DisplayName("업무 진척도 변경 테스트")
    fun change_task_progress_success(){
        `when`(postService.changeTaskContents(MockitoHelper.anyObject())).thenReturn(ObjectId())
        val data = TaskProgressRequest("1231232133", "40")

        mockMvc
                .perform(put("/task/progress")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data))
                )
                .andDo(print())
                .andExpect(status().isOk)
    }

    @Test
    @DisplayName("업무 진척도 변경 테스트 - 최소값 미만")
    fun change_task_progress_wrong_min_value(){
        `when`(postService.changeTaskContents(MockitoHelper.anyObject())).thenReturn(ObjectId())
        val data = TaskProgressRequest("1231232133", "-1")

        mockMvc
                .perform(put("/task/progress")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data))
                )
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("업무 진척도 변경 테스트 - 최대값 초과")
    fun change_task_progress_wrong_max_value(){
        `when`(postService.changeTaskContents(MockitoHelper.anyObject())).thenReturn(ObjectId())
        val data = TaskProgressRequest("1231232133", "101")

        mockMvc
                .perform(put("/task/progress")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(data))
                )
                .andDo(print())
                .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("업무 날짜 변경 테스트 - jwt 누락")
    fun change_task_date_omission_jwt(){
        mockMvc
                .perform(put("/task/progress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized)
    }

    @Test
    @DisplayName("업무 날짜 변경 테스트 - 둘다 null")
    fun change_task_date_success_all_null(){
        val taskDateRequest = TaskDateRequest("123", null, null)

        mockMvc
                .perform(put("/task/progress")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDateRequest))
                )
                .andDo(print())
                .andExpect(status().isOk)
    }

    @Test
    @DisplayName("업무 날짜 변경 테스트 - start date null")
    fun change_task_date_success_start_date_null(){
        val taskDateRequest = TaskDateRequest("123", null, LocalDateTime.parse("2021-04-10T00:00:00"))

        mockMvc
                .perform(put("/task/progress")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDateRequest))
                )
                .andDo(print())
                .andExpect(status().isOk)
    }

    @Test
    @DisplayName("업무 날짜 변경 테스트 - end date null")
    fun change_task_date_success_end_date_null(){
        val taskDateRequest = TaskDateRequest("123", LocalDateTime.parse("2021-04-10T00:00:00"), null)

        mockMvc
                .perform(put("/task/progress")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDateRequest))
                )
                .andDo(print())
                .andExpect(status().isOk)
    }
}

data class TestTaskRequest(
        val title: String
        , val taskStatus: String
        , val startDate: String
        , val endDate: String
        , val managers: List<String>
        , val progress: Int
        , val priority: String
        , val userId: String
        , val projectId: Long?
) {
}
