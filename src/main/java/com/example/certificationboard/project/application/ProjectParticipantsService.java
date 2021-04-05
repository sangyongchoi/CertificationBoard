package com.example.certificationboard.project.application;

import com.example.certificationboard.project.domain.ProjectParticipants;
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
}
