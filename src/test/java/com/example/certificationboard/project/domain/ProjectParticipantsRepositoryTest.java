package com.example.certificationboard.project.domain;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProjectParticipantsRepositoryTest {

    @Autowired
    ProjectParticipantsRepository projectParticipantsRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("기능 테스트")
    public void test() {
        // given
        Project project = new Project("test", "test", "test", "test");
        final Project save = projectRepository.save(project);

        final Member save1 = memberRepository.save(new Member("csytest1", "csytest1", "csytest", Boolean.FALSE));
        final ProjectParticipantsId projectParticipantsId = new ProjectParticipantsId(save, save1);
        final ProjectParticipantsId projectParticipantsId2 = new ProjectParticipantsId(save, save1);
        ProjectParticipants projectParticipants = new ProjectParticipants(projectParticipantsId, ProjectParticipants.Role.MEMBER);
        ProjectParticipants projectParticipants1 = new ProjectParticipants(projectParticipantsId2, ProjectParticipants.Role.MEMBER);
        projectParticipants.exit();
        // when
        final ProjectParticipants save2 = projectParticipantsRepository.save(projectParticipants);
        projectParticipantsRepository.flush();
        final ProjectParticipants save3 = projectParticipantsRepository.save(projectParticipants1);
        projectParticipantsRepository.flush();
        //then
        System.out.println(save2.getProjectParticipantsId().getProject().getId());
        System.out.println(save2.getProjectParticipantsId().getMember().getId());

        System.out.println("=========================");

        System.out.println(save3.getProjectParticipantsId().getProject().getId());
        System.out.println(save3.getProjectParticipantsId().getMember().getId());
    }

}