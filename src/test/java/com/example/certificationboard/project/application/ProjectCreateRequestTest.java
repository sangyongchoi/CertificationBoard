package com.example.certificationboard.project.application;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.project.domain.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProjectCreateRequestTest {

    @Test
    @DisplayName("ProjectCreateRequestTest => Project 변환 테스트")
    public void projectCreateRequest_to_project_convert_test() throws Exception{
        // given
        final String userId = "csytest1";
        final ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest(userId, "test", "test");
        Member member = new Member(userId, "csytest1", "test", false);

        // when
        final Project project = projectCreateRequest.toProjectEntity(member);

        //then
        assertEquals(projectCreateRequest.getUserId(), project.getCreatedMemberId());
        assertEquals(projectCreateRequest.getTitle(), project.getTitle());
        assertEquals(projectCreateRequest.getExplain(), project.getExplain());
        assertEquals(member.getOrganizationId(), project.getOrganizationId());
    }

}