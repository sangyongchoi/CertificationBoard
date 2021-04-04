package com.example.certificationboard.project.application;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.project.domain.*;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectParticipantsRepository projectParticipantsRepository;

    public ProjectService(ProjectRepository projectRepository, ProjectParticipantsRepository projectParticipantsRepository) {
        this.projectRepository = projectRepository;
        this.projectParticipantsRepository = projectParticipantsRepository;
    }

    public Long create(Project project, Member member) {
        final Project save = projectRepository.save(project);
        final ProjectParticipantsId projectParticipantsId = new ProjectParticipantsId(save, member);
        final ProjectParticipants projectParticipants = new ProjectParticipants(projectParticipantsId, ProjectParticipants.Role.ADMIN);

        projectParticipantsRepository.save(projectParticipants);

        return save.getId();
    }
}
