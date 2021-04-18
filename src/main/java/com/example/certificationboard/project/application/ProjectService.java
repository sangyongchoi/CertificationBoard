package com.example.certificationboard.project.application;

import com.example.certificationboard.common.util.PageUtil;
import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.project.domain.*;
import com.example.certificationboard.project.query.ProjectQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectParticipantsRepository projectParticipantsRepository;
    private final ProjectQueryRepository projectQueryRepository;

    public ProjectService(ProjectRepository projectRepository, ProjectParticipantsRepository projectParticipantsRepository, ProjectQueryRepository projectQueryRepository) {
        this.projectRepository = projectRepository;
        this.projectParticipantsRepository = projectParticipantsRepository;
        this.projectQueryRepository = projectQueryRepository;
    }

    @Transactional
    public Long create(Project project, Member member) {
        final Project createdProject = projectRepository.save(project);
        final ProjectParticipantsId projectParticipantsId = new ProjectParticipantsId(createdProject, member);
        final ProjectParticipants projectParticipants = new ProjectParticipants(projectParticipantsId, ProjectParticipants.Role.ADMIN, false);

        projectParticipantsRepository.save(projectParticipants);

        return createdProject.getId();
    }

    public Project findById(Long projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
    }

    public boolean existsProject(Long id){
        return projectRepository.existsById(id);
    }

    public ProjectPageResponse list(Pageable pageable, String memberId, boolean favorites){
        final Page<ProjectInfo> lists = projectQueryRepository.findLists(pageable, memberId, favorites);
        boolean hasNext = PageUtil.hasNext(lists, pageable);

        return new ProjectPageResponse(hasNext, lists.getContent());
    }
}
