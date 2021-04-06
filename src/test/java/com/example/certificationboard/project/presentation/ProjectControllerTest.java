package com.example.certificationboard.project.presentation;

import com.example.certificationboard.ControllerTest;
import com.example.certificationboard.TestUtil;
import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.project.application.ProjectCreateRequest;
import com.example.certificationboard.project.application.ProjectService;
import com.example.certificationboard.project.domain.Project;
import com.example.certificationboard.security.filter.JWTFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    ObjectMapper objectMapper;

    @BeforeEach
    void beforeSetup() {
        objectMapper = new ObjectMapper();
        String userId = "csytest1";
        final Member member1 = new Member("csytest1", "csytest1", "test", false);
        given(memberService.findMemberById(userId)).willReturn(member1);
        given(projectService.create(any(Project.class), any(Member.class))).willReturn(1L);
    }

    @Test
    @DisplayName("프로젝트 정상 생성 테스트")
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value(1L));
    }
}