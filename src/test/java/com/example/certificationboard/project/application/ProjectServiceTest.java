package com.example.certificationboard.project.application;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import com.example.certificationboard.project.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProjectServiceTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectParticipantsRepository projectParticipantsRepository;

    @Autowired
    MemberRepository memberRepository;

    ProjectService projectService;

    @BeforeEach
    void setup(){
        projectService = new ProjectService(projectRepository, projectParticipantsRepository);
        final Member member = new Member("csytest1", "csytest1", "csytest1", false);
        memberRepository.save(member);
    }

    @Test
    @DisplayName("프로젝트 생성 테스트")
    public void create_project_test() {
        // given
        final String userId = "csytest1";
        ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest(userId, "test", "test");
        final Member member = memberRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("잘못된 정보입니다."));
        final Project project = projectCreateRequest.toProjectEntity(member);
        final ProjectParticipantsId projectParticipantsId = new ProjectParticipantsId(project, member);

        // when
        Long id = projectService.create(project, member);
        final ProjectParticipants projectParticipants = projectParticipantsRepository.findById(projectParticipantsId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 정보입니다"));

        //then
        assertEquals(1, id);
        assertEquals(member.getId(), projectParticipants.getProjectParticipantsId().getMember().getId());
        assertEquals(project.getId(), projectParticipants.getProjectParticipantsId().getProject().getId());
    }

}