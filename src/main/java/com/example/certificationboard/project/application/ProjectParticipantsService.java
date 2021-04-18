package com.example.certificationboard.project.application;

import com.example.certificationboard.project.domain.ProjectParticipants;
import com.example.certificationboard.project.domain.ProjectParticipantsId;
import com.example.certificationboard.project.domain.ProjectParticipantsRepository;
import org.springframework.stereotype.Service;

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
        return projectParticipantsRepository.findById(participantsId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 값입니다."));
    }

    public ProjectParticipants addFavorite(ProjectParticipantsId participantsId){
        final ProjectParticipants projectParticipants = findParticipants(participantsId);

        projectParticipants.addFavorite();
        projectParticipantsRepository.save(projectParticipants);

        return projectParticipants;
    }

}
