package com.example.certificationboard.project.application;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import com.example.certificationboard.project.domain.*;
import com.example.certificationboard.project.query.ProjectQueryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    ProjectParticipantsService projectParticipantsService;
    ProjectService projectService;

    @BeforeAll
    void setup(){
        projectParticipantsService = new ProjectParticipantsService(projectParticipantsRepository);
        projectService = new ProjectService(projectRepository, projectParticipantsService, projectQueryRepository);

        final Member member = new Member("csytest1", "csytest1", "csytest1", false);
        memberRepository.save(member);

        make100Project();
    }

    private void make100Project(){
        final String userId = "csytest1";

        for (int i = 1; i <= 100; i++) {
            ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest(userId, "test" + i, "test");
            final Member member = memberRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("잘못된 정보입니다."));
            final Project project = projectCreateRequest.toProjectEntity(member);
            Long id = projectService.create(project, member);

        }
        projectRepository.flush();
    }


    @Test
    @DisplayName("프로젝트 리스트 조회")
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
    @DisplayName("프로젝트 생성 테스트")
    @Order(4)
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
        assertNotNull(id);
        assertEquals(member.getId(), projectParticipants.getProjectParticipantsId().getMember().getId());
        assertEquals(project.getId(), projectParticipants.getProjectParticipantsId().getProject().getId());
    }

    @Test
    @DisplayName("id로 프로젝트 조회 테스트")
    @Order(5)
    public void findById() {
        // given
        Long id = 1L;

        // when
        final Project byId = projectService.findById(id);

        //then
        assertEquals(1, byId.getId());
        assertEquals("test1", byId.getTitle());
    }
}