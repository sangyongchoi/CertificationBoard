package com.example.certificationboard.projectparticipants.application;

import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import com.example.certificationboard.project.application.ProjectCreateRequest;
import com.example.certificationboard.project.application.ProjectInviteRequest;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
//@DataJpaTest
@SpringBootTest
@Rollback(value = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    Long projectId;

    String inviteeMemberId = "invitetest";

    String notParticipantsId = "notParticipants";

    @BeforeAll
    void setup(){
        projectService = new ProjectService(projectRepository, projectQueryRepository);
        projectParticipantsService = new ProjectParticipantsService(projectParticipantsRepository, projectService, memberService);

        final Member member = new Member("csytest1", "csytest1", "csytest1", false);
        final Member member2 = new Member("csytest2", "csytest1", "csytest2", false);
        final Member member3 = new Member("csytest3", "csytest1", "csytest3", false);
        final Member inviteeMember = new Member(inviteeMemberId, "csytest1", "csytest3", false);
        final Member notParticipants = new Member(notParticipantsId, "csytest1", "csytest3", false);

        memberRepository.save(member);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(inviteeMember);
        memberRepository.save(notParticipants);

        final String userId = "csytest1";
        ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest(userId, "test1", "test");
        final Project project = projectCreateRequest.toProjectEntity(member);
        final Project createdProject = projectService.create(project);

        projectId = createdProject.getId();

        final ProjectParticipantsId adminParticipants = new ProjectParticipantsId(createdProject, member);
        projectParticipantsRepository.save(new ProjectParticipants(adminParticipants, ProjectParticipants.Role.ADMIN, false));
        projectParticipantsRepository.flush();

        final ProjectParticipantsId projectParticipantsId = new ProjectParticipantsId(createdProject, member3);
        projectParticipantsRepository.save(new ProjectParticipants(projectParticipantsId, ProjectParticipants.Role.MEMBER, true));
        projectParticipantsRepository.flush();
    }

    @Test
    @DisplayName("???????????? ???????????? ???")
    @Order(1)
    public void is_project_participants() {
        // given
        final Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("???????????? ?????? ?????????????????????."));
        final Member member = memberRepository.findById("csytest1").orElseThrow(() -> new IllegalArgumentException("????????? ???????????????."));

        // when
        projectParticipantsService.validateParticipants(project.getId(), member.getId());
    }

    @Test
    @DisplayName("???????????? ????????? ?????? ???")
    @Order(2)
    public void not_project_participants() {
        assertThrows(NotParticipantsException.class, () -> {
            // given
            final Project project = projectRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("???????????? ?????? ?????????????????????."));
            final Member member = memberRepository.findById("csytest2").orElseThrow(() -> new IllegalArgumentException("????????? ???????????????."));

            // when
            projectParticipantsService.validateParticipants(project.getId(), member.getId());
        });
    }

    @Test
    @DisplayName("???????????? ????????? ????????? ??????")
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
    @DisplayName("???????????? ???????????????")
    @Order(4)
    public void project_participants_join_test() {
        // given
        final Project project = projectRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("???????????? ?????? ?????????????????????."));
        final Member member = memberRepository.findById("csytest2").orElseThrow(() -> new IllegalArgumentException("????????? ???????????????."));
        final ProjectParticipantsId projectParticipantsId = new ProjectParticipantsId(project, member);

        // when
        final ProjectParticipants join = projectParticipantsService.join(projectParticipantsId, ProjectParticipants.Role.MEMBER);

        // then
        assertEquals(join.getProjectParticipantsId().getMember().getId(), "csytest2");
    }

    @Test
    @DisplayName("???????????? ????????? ?????? ?????????")
    @Order(5)
    public void find_project_participants() {
        // given
        final Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("???????????? ?????? ?????????????????????."));
        final Member member = memberRepository.findById("csytest1").orElseThrow(() -> new IllegalArgumentException("????????? ???????????????."));
        final ProjectParticipantsId projectParticipantsId = new ProjectParticipantsId(project, member);

        // when
        final ProjectParticipants participants = projectParticipantsService.getParticipants(projectParticipantsId);

        //then
        assertEquals("csytest1", participants.getProjectParticipantsId().getMember().getId());
        assertEquals(1, participants.getProjectParticipantsId().getProject().getId());
    }

    @Test
    @DisplayName("???????????? ?????? ?????????")
    @Order(6)
    public void addFavorite() {
        // given
        final Member member = memberRepository.findById("csytest1").orElseThrow(() -> new IllegalArgumentException("????????? ???????????????."));

        // when
        final ProjectParticipants projectParticipants = projectParticipantsService.addFavorite(member, projectId);

        //then
        assertTrue(projectParticipants.isFavorites());
    }

    @Test
    @DisplayName("????????? ?????? ????????????")
    @Order(7)
    public void get_managers_info() {
        // given
        List<String> managers = Arrays.asList("csytest1", "csytest2", "csytest3");

        // when
        final List<Member> managersInfo = projectParticipantsService.getUsersInfo(managers);

        //then
        assertEquals(3, managers.size());
    }

    @Test
    @DisplayName("???????????? ?????? - ???????????? ?????? ????????? ???????????? ???")
    @Order(8)
    public void invite_project_fail() {
        assertThrows(NotParticipantsException.class, () -> {
            final ProjectInviteRequest inviteRequest = new ProjectInviteRequest(projectId, inviteeMemberId, notParticipantsId);
            projectParticipantsService.invite(inviteRequest);
        });
    }

    @Test
    @DisplayName("???????????? ??????")
    @Order(9)
    public void invite_project() {
        final ProjectInviteRequest inviteRequest = new ProjectInviteRequest(projectId, inviteeMemberId, "csytest1");
        projectParticipantsService.invite(inviteRequest);

        // success
    }

    @Test
    @DisplayName("???????????? ??????")
    @Order(10)
    public void deleteFavorite() {
        // given
        final Member member = memberRepository.findById("csytest3").orElseThrow(() -> new IllegalArgumentException("????????? ???????????????."));

        // when
        projectParticipantsService.deleteFavorite(member, projectId);

        // success
    }

}