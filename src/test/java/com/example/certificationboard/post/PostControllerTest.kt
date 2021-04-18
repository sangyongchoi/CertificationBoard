package com.example.certificationboard.post

import com.example.certificationboard.ControllerTest
import com.example.certificationboard.post.application.PostInfo
import com.example.certificationboard.post.application.PostResponse
import com.example.certificationboard.post.application.PostService
import com.example.certificationboard.post.domain.Post
import com.example.certificationboard.post.domain.PostRepository
import com.example.certificationboard.post.domain.TaskContents
import com.example.certificationboard.project.application.ProjectService
import com.example.certificationboard.security.filter.JWTFilter
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime


@WebMvcTest(controllers = [PostController::class])
@MockBean(JpaMetamodelMappingContext::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PostControllerTest: ControllerTest(){

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var postService: PostService

    @MockBean
    lateinit var projectService: ProjectService

    @MockBean
    lateinit var postRepository: PostRepository

    @BeforeAll
    fun whenSetup(){
        `when`(postService.findList(any(), anyString(), anyLong())).thenReturn(getDummyResponse())
    }

    private fun getDummyResponse(): PostResponse {
        var hasNext = false;
        var postInfos = mutableListOf<PostInfo>()

        postInfos.add(PostInfo(ObjectId.get(), 1L, Post.Type.TASK, TaskContents("test", LocalDateTime.of(2021, 4, 10, 0, 0), LocalDateTime.of(2021, 4, 11, 23, 59))))
        postInfos.add(PostInfo(ObjectId.get(), 1L, Post.Type.TASK, TaskContents("test", LocalDateTime.of(2021, 4, 10, 0, 0), LocalDateTime.of(2021, 4, 11, 23, 59))))
        postInfos.add(PostInfo(ObjectId.get(), 1L, Post.Type.TASK, TaskContents("test", LocalDateTime.of(2021, 4, 10, 0, 0), LocalDateTime.of(2021, 4, 11, 23, 59))))

        return PostResponse(hasNext, postInfos)
    }

    @Test
    @DisplayName("토큰 누락 테스트")
    fun jwt_ommission_test(){
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

    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }
}