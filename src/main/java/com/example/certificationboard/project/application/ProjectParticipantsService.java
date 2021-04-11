package com.example.certificationboard.project.application;

import com.example.certificationboard.common.util.PageUtil;
import com.example.certificationboard.project.domain.Project;
import com.example.certificationboard.project.domain.ProjectParticipants;
import com.example.certificationboard.project.domain.ProjectParticipantsId;
import com.example.certificationboard.project.domain.ProjectParticipantsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectParticipantsService {

    private final ProjectParticipantsRepository projectParticipantsRepository;

    public ProjectParticipantsService(ProjectParticipantsRepository projectParticipantsRepository) {
        this.projectParticipantsRepository = projectParticipantsRepository;
    }

    public ProjectParticipants join(ProjectParticipants participants){
        return projectParticipantsRepository.save(participants);
    }

    public ProjectParticipants findParticipants(ProjectParticipantsId participantsId){
        return projectParticipantsRepository.getOne(participantsId);
    }

    public ProjectPageResponse list(Pageable pageable, boolean isFavorite) {
        final Page<ProjectParticipants> findProject = projectParticipantsRepository.findAllByFavorites(pageable, isFavorite);
        boolean hasNext = PageUtil.hasNext(findProject, pageable);

        //return new ProjectPageResponse(hasNext, projectList);
        return null;
    }

}
