package com.example.certificationboard.project.application;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.project.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public Project findById(Long id){
        return projectRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("잘못된 인자입니다."));
    }

    public ProjectResponse list(Pageable pageable, boolean isFavorite) {
        final Page<Project> findProject = projectRepository.findAllByFavorites(pageable, isFavorite);
        boolean hasNext = pageable.getPageNumber() + 1 < findProject.getTotalPages();
        final List<ProjectDto> projectList = findProject.stream()
                .map(ProjectDto::of)
                .collect(Collectors.toList());

        return new ProjectResponse(hasNext, projectList);
    }

    public boolean addFavorite(Project project){
        project.addFavorites();
        return changeFavorite(project);
    }

    public boolean deleteFavorite(Project project){
        project.deleteFavorites();
        return changeFavorite(project);
    }

    private boolean changeFavorite(Project project){
        projectRepository.save(project);

        return project.isFavorites();
    }

}
