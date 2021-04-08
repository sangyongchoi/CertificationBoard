package com.example.certificationboard.project.application;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.project.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectParticipantsService projectParticipantsService;

    public ProjectService(ProjectRepository projectRepository, ProjectParticipantsService projectParticipantsService) {
        this.projectRepository = projectRepository;
        this.projectParticipantsService = projectParticipantsService;
    }

    @Transactional
    public Long create(Project project, Member member) {
        final Project createdProject = projectRepository.save(project);
        final ProjectParticipantsId projectParticipantsId = new ProjectParticipantsId(createdProject, member);
        final ProjectParticipants projectParticipants = new ProjectParticipants(projectParticipantsId, ProjectParticipants.Role.ADMIN);

        projectParticipantsService.join(projectParticipants);

        return createdProject.getId();
    }

    public Page<Project> list(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }
}
