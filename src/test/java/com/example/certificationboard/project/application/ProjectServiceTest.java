package com.example.certificationboard.project.application;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import com.example.certificationboard.project.domain.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProjectServiceTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectParticipantsRepository projectParticipantsRepository;

    @Autowired
    MemberRepository memberRepository;

    ProjectParticipantsService projectParticipantsService;
    ProjectService projectService;

    @BeforeAll
    void setup(){
        projectParticipantsService = new ProjectParticipantsService(projectParticipantsRepository);
        projectService = new ProjectService(projectRepository, projectParticipantsService);

        final Member member = new Member("csytest1", "csytest1", "csytest1", false);
        memberRepository.save(member);

        make100Project();
    }

    private void make100Project(){
        final String userId = "csytest1";

        for (int i = 0; i < 100; i++) {
            ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest(userId, "test", "test");
            final Member member = memberRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("잘못된 정보입니다."));
            final Project project = projectCreateRequest.toProjectEntity(member);
            Long id = projectService.create(project, member);

            projectRepository.flush();
        }
    }

    @Test
    @DisplayName("프로젝트 생성 테스트")
    @Order(3)
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
    @DisplayName("페이징 처리 테스트 - 1페이지")
    @Order(1)
    public void get_project_list(){
        // given
        Pageable pageable = PageRequest.of(0, 20);

        // when
        final Page<Project> list = projectService.list(pageable);
        final List<Project> content = list.getContent();

        //then
        assertEquals(1, content.get(0).getId());
        assertEquals(20, list.getSize());
    }

    @Test
    @DisplayName("페이징 처리 테스트 - 2페이지")
    @Order(2)
    public void get_project_list_2page(){
        // given
        Pageable pageable = PageRequest.of(1, 20);

        // when
        final Page<Project> list = projectService.list(pageable);
        final List<Project> content = list.getContent();

        //then
        assertEquals(21, content.get(0).getId());
        assertEquals(20, list.getSize());
    }

}