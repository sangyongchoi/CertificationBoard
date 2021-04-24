package com.example.certificationboard.projectparticipants.application;

import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import com.example.certificationboard.project.application.ProjectCreateRequest;
import com.example.certificationboard.project.application.ProjectService;
import com.example.certificationboard.project.domain.Project;
import com.example.certificationboard.project.domain.ProjectRepository;
import com.example.certificationboard.project.query.ProjectQueryRepository;
import com.example.certificationboard.projectparticipants.domain.ProjectParticipants;
import com.example.certificationboard.projectparticipants.domain.ProjectParticipantsId;
import com.example.certificationboard.projectparticipants.domain.ProjectParticipantsRepository;
import com.example.certificationboard.projectparticipants.exception.NotParticipantsException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
//@DataJpaTest
@SpringBootTest
@Rollback(value = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectParticipantsServiceTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectParticipantsRepository projectParticipantsRepository;

    @Autowired
    ProjectQueryRepository projectQueryRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    ProjectParticipantsService projectParticipantsService;
    ProjectService projectService;

    @BeforeAll
    void setup(){
        projectService = new ProjectService(projectRepository, projectParticipantsRepository, projectQueryRepository);
        projectParticipantsService = new ProjectParticipantsService(projectParticipantsRepository, projectService, memberService);

        final Member member = new Member("csytest1", "csytest1", "csytest1", false);
        final Member member2 = new Member("csytest2", "csytest1", "csytest2", false);
        final Member member3 = new Member("csytest3", "csytest1", "csytest3", false);
        memberRepository.save(member);
        memberRepository.save(member2);
        memberRepository.save(member3);

        final String userId = "csytest1";
        ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest(userId, "test1", "test");
        final Project project = projectCreateRequest.toProjectEntity(member);
        Long id = projectService.create(project, member);

        final ProjectParticipantsId projectParticipantsId = new ProjectParticipantsId(project, member3);
        projectParticipantsRepository.save(new ProjectParticipants(projectParticipantsId, ProjectParticipants.Role.MEMBER, false));
        projectParticipantsRepository.flush();
    }

    @Test
    @DisplayName("프로젝트 참여자일 때")
    @Order(1)
    public void is_project_participants() {
        // given
        final Project project = projectRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        final Member member = memberRepository.findById("csytest1").orElseThrow(() -> new IllegalArgumentException("잘못된 정보입니다."));

        // when
        projectParticipantsService.validateParticipants(project.getId(), member.getId());
    }

    @Test
    @DisplayName("프로젝트 참여자 아닐 때")
    @Order(2)
    public void not_project_participants() {
        assertThrows(NotParticipantsException.class, () -> {
            // given
            final Project project = projectRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
            final Member member = memberRepository.findById("csytest2").orElseThrow(() -> new IllegalArgumentException("잘못된 정보입니다."));

            // when
            projectParticipantsService.validateParticipants(project.getId(), member.getId());
        });
    }

    @Test
    @DisplayName("프로젝트 참여자 리스트 조회")
    @Order(3)
    public void find_project_participants_list() {
        // given
        Long projectId = 1L;

        // when
        List<ProjectParticipantsDto> participantList = projectParticipantsService.getProjectParticipantsList(projectId);

        //then
        assertEquals(2, participantList.size());
    }

    @Test
    @DisplayName("프로젝트 참여테스트")
    @Order(4)
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
        assertEquals(join.getProjectParticipantsId().getMember().getId(), "csytest2");
    }

    @Test
    @DisplayName("프로젝트 참여자 조회 테스트")
    @Order(5)
    public void find_project_participants() {
        // given
        final Project project = projectRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        final Member member = memberRepository.findById("csytest1").orElseThrow(() -> new IllegalArgumentException("잘못된 정보입니다."));
        final ProjectParticipantsId projectParticipantsId = new ProjectParticipantsId(project, member);

        // when
        final ProjectParticipants participants = projectParticipantsService.getParticipants(projectParticipantsId);

        //then
        assertEquals("csytest1", participants.getProjectParticipantsId().getMember().getId());
        assertEquals(1, participants.getProjectParticipantsId().getProject().getId());
    }

    @Test
    @DisplayName("즐겨찾기 추가 테스트")
    @Order(6)
    public void addFavorite() {
        // given
        final Project project = projectRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        final Member member = memberRepository.findById("csytest1").orElseThrow(() -> new IllegalArgumentException("잘못된 정보입니다."));
        final ProjectParticipantsId projectParticipantsId = new ProjectParticipantsId(project, member);

        // when
        final ProjectParticipants projectParticipants = projectParticipantsService.addFavorite(projectParticipantsId);

        //then
        final ProjectParticipants participants = projectParticipantsService.getParticipants(projectParticipantsId);
        assertTrue(participants.isFavorites());
    }

}