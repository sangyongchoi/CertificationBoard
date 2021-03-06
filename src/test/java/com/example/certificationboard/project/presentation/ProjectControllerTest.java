package com.example.certificationboard.project.presentation;

import com.example.certificationboard.ControllerTest;
import com.example.certificationboard.TestUtil;
import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.project.application.ProjectCreateRequest;
import com.example.certificationboard.project.application.ProjectInfo;
import com.example.certificationboard.project.application.ProjectPageResponse;
import com.example.certificationboard.project.application.ProjectService;
import com.example.certificationboard.project.domain.Project;
import com.example.certificationboard.projectparticipants.application.ProjectParticipantsService;
import com.example.certificationboard.projectparticipants.domain.ProjectParticipantsRepository;
import com.example.certificationboard.security.filter.JWTFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProjectController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ProjectControllerTest extends ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProjectService projectService;

    @MockBean
    MemberService memberService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProjectParticipantsService projectParticipantsService;

    @MockBean
    ProjectParticipantsRepository projectParticipantsRepository;

    @BeforeEach
    void beforeSetup() {
        String userId = "csytest1";
        jwt = TestUtil.createToken();
        final Member member1 = new Member("csytest1", "csytest1", "test", false);
        given(memberService.findById(userId)).willReturn(member1);
        given(projectService.list(any(Pageable.class),anyString() ,any(boolean.class))).willReturn(findProjectList());
        given(projectService.create(any(Project.class))).willReturn(new Project());
    }

    private ProjectPageResponse findProjectList(){
        List<ProjectInfo> projectInfoList = new ArrayList<>();

        for (long i = 1; i <= 20; i++) {
            projectInfoList.add(new ProjectInfo(i , "test", "test"));
        }

        return new ProjectPageResponse(true, projectInfoList);
    }

    @Test
    @DisplayName("???????????? ?????? ?????? ?????????")
    public void project_create_test() throws Exception {
        // given
        final ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest("csytest1", "test", "test");

        // when
        mockMvc
                .perform(post("/project")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectCreateRequest))
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    public void jwt_omission_test() throws Exception {
        // given
        final ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest("csytest1", "test", "test");

        // when
        mockMvc
                .perform(post("/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectCreateRequest))
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("??????ID ?????? ?????????")
    public void userId_omission_test() throws Exception {
        // given
        final ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest("", "test", "test");

        // when
        mockMvc
                .perform(post("/project")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectCreateRequest))
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("???????????? ?????? ?????? ?????????")
    public void project_title_omission_test() throws Exception {
        // given
        final ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest("csytest1", "", "test");

        // when
        mockMvc
                .perform(post("/project")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectCreateRequest))
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("?????? ???????????? ????????? ???????????? ?????????")
    public void get_project_list() throws Exception {
        // given
        String memberId = "csytest1";
        String size = "20";
        String page = "0";

        // when
        mockMvc
                .perform(get("/normal")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("userId", memberId)
                        .param("page", page)
                        .param("size", size)
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("?????? ???????????? ????????? - jwt ??????")
    public void get_project_list_omission_jwt() throws Exception {
        // given
        String size = "20";
        String page = "0";

        // when
        mockMvc
                .perform(get("/normal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", page)
                        .param("size", size)
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("?????? ???????????? ????????? - page ?????? ?????? ??? ???????????? ???????????? ?????????")
    public void get_project_list_omission_pageinfo() throws Exception {
        String memberId = "csytest1";

        // when
        mockMvc
                .perform(get("/normal")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("userId", memberId)
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("???????????? ???????????? ????????? ???????????? ?????????")
    public void get_favorite_project_list() throws Exception {
        // given
        String memberId = "csytest1";
        String size = "20";
        String page = "0";

        // when
        mockMvc
                .perform(get("/favorite")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("userId", memberId)
                        .param("page", page)
                        .param("size", size)
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("?????? ???????????? ????????? - jwt ??????")
    public void get_favorite_project_list_omission_jwt() throws Exception {
        // given
        String size = "20";
        String page = "0";

        // when
        mockMvc
                .perform(get("/favorite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", page)
                        .param("size", size)
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("?????? ???????????? ????????? - page ?????? ?????? ??? ???????????? ???????????? ?????????")
    public void get_favorite_project_list_omission_pageinfo() throws Exception {
        String memberId = "csytest1";

        // when
        mockMvc
                .perform(get("/favorite")
                        .header(JWTFilter.AUTH_HEADER_NAME, jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("userId", memberId)
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isOk());
    }

}