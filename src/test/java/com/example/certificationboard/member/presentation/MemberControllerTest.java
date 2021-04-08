package com.example.certificationboard.member.presentation;

import com.example.certificationboard.ControllerTest;
import com.example.certificationboard.member.application.MemberRequest;
import com.example.certificationboard.member.application.MemberService;
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

@WebMvcTest(controllers = { MemberController.class })
@MockBean(JpaMetamodelMappingContext.class)
class MemberControllerTest extends ControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void beforeSetup() {
        MemberRequest member = new MemberRequest("csytest11", "csytest", "csytest");
        given(memberService.signUp(any())).willReturn(member.toMemberEntity());
    }

    @Test
    @DisplayName("회원가입 정상 테스트")
    public void signup() throws Exception {
        // given
        MemberRequest member = new MemberRequest("csytest11", "csytest111111", "csytest");

        // when
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member))
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(member.getId()));
    }

    @Test
    @DisplayName("validation 실패 테스트 - id가 null일 때")
    public void validation_fail_when_id_null() throws Exception {
        // given
        MemberRequest member = new MemberRequest(null, "csytest", "csytest");

        // when
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member))
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("validation 실패 테스트 - id가 8글자 미만 일 때")
    public void validation_fail_when_less_than_min() throws Exception {
        // given
        MemberRequest member = new MemberRequest("csytest", "csytest123", "csytest");

        // when
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member))
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("validation 실패 테스트 - id가 빈값 일 때")
    public void validation_fail_when_id_empty() throws Exception {
        // given
        MemberRequest member = new MemberRequest("", "csytest", "csytest");

        // when
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member))
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("validation 실패 테스트 - password가 null일 때")
    public void validation_fail_when_password_null() throws Exception {
        // given
        MemberRequest member = new MemberRequest("", null, "csytest");

        // when
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member))
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("validation 실패 테스트 - password가 빈값일 때")
    public void validation_fail_when_password_empty() throws Exception {
        // given
        MemberRequest member = new MemberRequest("", "", "csytest");

        // when
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member))
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("validation 실패 테스트 - password가 8글자 미만일 때")
    public void validation_fail_when_password_less_than_min() throws Exception {
        // given
        MemberRequest member = new MemberRequest("csytest11", "csytest", "csytest");

        // when
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member))
                        .with(csrf())
                )
                .andDo(print())
                //then
                .andExpect(status().isBadRequest());
    }

}