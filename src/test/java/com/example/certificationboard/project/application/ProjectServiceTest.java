package com.example.certificationboard.project.application;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import com.example.certificationboard.project.domain.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

        for (int i = 1; i <= 100; i++) {
            ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest(userId, "test" + i, "test");
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
    @DisplayName("id로 프로젝트 조회 테스트")
    public void findById() {
        // given
        Long id = 1L;

        // when
        final Project byId = projectService.findById(id);

        //then
        assertEquals(1, byId.getId());
        assertEquals("test1", byId.getTitle());
    }

    @Test
    @DisplayName("페이징 처리 테스트 - 1페이지")
    @Order(1)
    public void get_project_list(){
        // given
        Pageable pageable = PageRequest.of(0, 20);
        boolean isFavorites = false;

        // when
        final ProjectResponse projectListInfo = projectService.list(pageable, isFavorites);
        final List<ProjectDto> projectList = projectListInfo.getProjectList();

        //then
        assertEquals(1, projectList.get(0).getId());
        assertTrue(projectListInfo.isHasNext());
        assertEquals(20, projectList.size());
    }

    @Test
    @DisplayName("페이징 처리 테스트 - 2페이지")
    @Order(2)
    public void get_project_list_2page(){
        // given
        Pageable pageable = PageRequest.of(1, 20);
        boolean isFavorite = false;

        // when
        final ProjectResponse projectListInfo = projectService.list(pageable, isFavorite);
        final List<ProjectDto> projectList = projectListInfo.getProjectList();

        //then
        assertEquals(21, projectList.get(0).getId());
        assertTrue(projectListInfo.isHasNext());
        assertEquals(20, projectList.size());
    }

    @Test
    @DisplayName("페이징 처리 테스트 - 다음페이지 여부 false 테스트")
    @Order(2)
    public void get_project_list_when_hasNext_false(){
        // given
        Pageable pageable = PageRequest.of(5, 20);
        boolean isFavorite = false;

        // when
        final ProjectResponse projectListInfo = projectService.list(pageable, isFavorite);

        //then
        assertFalse(projectListInfo.isHasNext());
    }

    @Test
    @DisplayName("즐겨찾기 추가 테스트")
    public void project_add_favorite() {
        // given
        final Project project = projectRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 값입니다."));

        // when
        final boolean isFavorite = projectService.addFavorite(project);

        // then
        assertTrue(isFavorite);
    }

    @Test
    @DisplayName("즐겨찾기 제거 테스트")
    public void project_delete_favorite() {
        // given
        final Project project = projectRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 값입니다."));

        // when
        final boolean isFavorite = projectService.deleteFavorite(project);

        // then
        assertFalse(isFavorite);
    }

}