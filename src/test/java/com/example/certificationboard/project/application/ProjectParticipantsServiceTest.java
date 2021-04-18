package com.example.certificationboard.project.application;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import com.example.certificationboard.project.domain.*;
import com.example.certificationboard.project.query.ProjectQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
//@DataJpaTest
@SpringBootTest
@Rollback(value = false)
class ProjectParticipantsServiceTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectParticipantsRepository projectParticipantsRepository;

    @Autowired
    ProjectQueryRepository projectQueryRepository;

    @Autowired
    MemberRepository memberRepository;

    ProjectParticipantsService projectParticipantsService;
    ProjectService projectService;

    @BeforeEach
    void setup(){
        projectParticipantsService = new ProjectParticipantsService(projectParticipantsRepository);
        projectService = new ProjectService(projectRepository, projectParticipantsService, projectQueryRepository);
        final Member member = new Member("csytest1", "csytest1", "csytest1", false);
        final Member member2 = new Member("csytest2", "csytest1", "csytest1", false);
        memberRepository.save(member);
        memberRepository.save(member2);

        final String userId = "csytest1";
        ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest(userId, "test1", "test");
        final Project project = projectCreateRequest.toProjectEntity(member);
        Long id = projectService.create(project, member);
    }

    @Test
    @DisplayName("프로젝트 참여테스트")
    public void project_participants_join_test() {
        // given
        final Project project = projectRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        final Member member = memberRepository.findById("csytest2").orElseThrow(() -> new IllegalArgumentException("잘못된 정보입니다."));
        final ProjectParticipantsId projectParticipantsId = new ProjectParticipantsId(project, member);
        final ProjectParticipants projectParticipants = new ProjectParticipants(projectParticipantsId, ProjectParticipants.Role.MEMBER, false);

        // when
        final ProjectParticipants join = projectParticipantsService.join(projectParticipants);

        // then
        assertEquals(projectParticipants.getProjectParticipantsId(), join.getProjectParticipantsId());
    }

    @Test
    @DisplayName("프로젝트 참여자 조회 테스트")
    public void find_project_participants() {
        // given
        final Project project = projectRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        final Member member = memberRepository.findById("csytest1").orElseThrow(() -> new IllegalArgumentException("잘못된 정보입니다."));
        final ProjectParticipantsId projectParticipantsId = new ProjectParticipantsId(project, member);

        // when
        final ProjectParticipants participants = projectParticipantsService.findParticipants(projectParticipantsId);

        //then
        assertEquals("csytest1", participants.getProjectParticipantsId().getMember().getId());
        assertEquals(1, participants.getProjectParticipantsId().getProject().getId());
    }

    @Test
    @DisplayName("즐겨찾기 추가 테스트")
    public void addFavorite() {
        // given
        final Project project = projectRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        final Member member = memberRepository.findById("csytest1").orElseThrow(() -> new IllegalArgumentException("잘못된 정보입니다."));
        final ProjectParticipantsId projectParticipantsId = new ProjectParticipantsId(project, member);

        // when
        final ProjectParticipants projectParticipants = projectParticipantsService.addFavorite(projectParticipantsId);

        //then
        final ProjectParticipants participants = projectParticipantsService.findParticipants(projectParticipantsId);
        assertTrue(participants.isFavorites());
    }

}