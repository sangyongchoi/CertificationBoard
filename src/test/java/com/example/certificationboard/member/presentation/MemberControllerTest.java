package com.example.certificationboard.member.presentation;

import com.example.certificationboard.member.application.MemberRequest;
import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.member.domain.Member;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MemberService memberService;

    ObjectMapper objectMapper;

    @BeforeEach
    void beforeSetup(){
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("회원가입 정상 테스트")
    public void signup() throws Exception {
        // given
        MemberRequest member = new MemberRequest("csytest", "csytest", "csytest");
        given(memberService.signUp(any())).willReturn(member.getId());

        // when
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member))
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
        given(memberService.signUp(any())).willReturn(member.getId());

        // when
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member))
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
        given(memberService.signUp(any())).willReturn(member.getId());

        // when
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member))
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
        given(memberService.signUp(any())).willReturn(member.getId());

        // when
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member))
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
        given(memberService.signUp(any())).willReturn(member.getId());

        // when
        mockMvc
                .perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(member))
                )
                .andDo(print())
                //then
                .andExpect(status().isBadRequest());
    }

}