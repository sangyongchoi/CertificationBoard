package com.example.certificationboard.project.application;

import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import com.example.certificationboard.project.domain.*;
import com.example.certificationboard.project.query.ProjectQueryRepository;
import com.example.certificationboard.projectparticipants.domain.ProjectParticipants;
import com.example.certificationboard.projectparticipants.domain.ProjectParticipantsId;
import com.example.certificationboard.projectparticipants.domain.ProjectParticipantsRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
//@DataJpaTest
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectServiceTest {

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

    ProjectService projectService;

    @BeforeAll
    void setup(){
        projectService = new ProjectService(projectRepository, projectQueryRepository);

        final Member member = new Member("csytest1", "csytest1", "csytest1", false);
        memberRepository.save(member);

        make100Project();
    }

    private void make100Project(){
        final String userId = "csytest1";

        for (int i = 1; i <= 100; i++) {
            ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest(userId, "test" + i, "test");
            final Member member = memberRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("????????? ???????????????."));
            final Project project = projectCreateRequest.toProjectEntity(member);
            projectService.create(project);

        }
        projectRepository.flush();
    }


    @Test
    @DisplayName("???????????? ????????? ??????")
    @Order(1)
    public void find_project_list() throws Exception{
        // given
        String memberId = "csytest1";
        final Pageable pageable = PageRequest.of(0, 20);

        // when
        final Page<ProjectInfo> lists = projectQueryRepository.findLists(pageable, memberId, false);

        // then
        lists.forEach(System.out::println);
    }

    @Test
    @DisplayName("???????????? ?????? ?????????")
    @Order(4)
    public void create_project_test() {
        // given
        final String userId = "csytest1";
        ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest(userId, "test", "test");
        final Member member = memberRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("????????? ???????????????."));
        final Project project = projectCreateRequest.toProjectEntity(member);

        // when
        Project createdProject = projectService.create(project);

        //then
        assertNotNull(createdProject.getId());
    }

    @Test
    @DisplayName("id??? ???????????? ?????? ?????????")
    @Order(6)
    public void exists_project() {
        // given
        Long id = 1L;

        // when
        boolean existsProject = projectService.existsProject(id);

        //then
        assertTrue(existsProject);
    }

    @Test
    @DisplayName("id??? ???????????? ?????? ?????? ?????????")
    @Order(6)
    public void not_exists_project() {
        // given
        Long id = 1000L;

        // when
        boolean existsProject = projectService.existsProject(id);

        //then
        assertFalse(existsProject);
    }
}