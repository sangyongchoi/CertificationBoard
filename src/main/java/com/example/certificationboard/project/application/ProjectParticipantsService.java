package com.example.certificationboard.project.application;

import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.project.domain.Project;
import com.example.certificationboard.project.domain.ProjectParticipants;
import com.example.certificationboard.project.domain.ProjectParticipantsId;
import com.example.certificationboard.project.domain.ProjectParticipantsRepository;
import com.example.certificationboard.project.exception.NotParticipantsException;
import org.springframework.stereotype.Service;

@Service
public class ProjectParticipantsService {

    private final ProjectParticipantsRepository projectParticipantsRepository;
    private final ProjectService projectService;
    private final MemberService memberService;

    public ProjectParticipantsService(ProjectParticipantsRepository projectParticipantsRepository, ProjectService projectService, MemberService memberService) {
        this.projectParticipantsRepository = projectParticipantsRepository;
        this.projectService = projectService;
        this.memberService = memberService;
    }

    public ProjectParticipants join(ProjectParticipants participants){
        return projectParticipantsRepository.save(participants);
    }

    public ProjectParticipants addFavorite(ProjectParticipantsId participantsId){
        final ProjectParticipants projectParticipants = findParticipants(participantsId);

        projectParticipants.addFavorite();
        projectParticipantsRepository.save(projectParticipants);

        return projectParticipants;
    }

    public ProjectParticipants findParticipants(ProjectParticipantsId participantsId){
        return projectParticipantsRepository.findById(participantsId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 값입니다."));
    }

    public void isProjectParticipants(Long projectId, String userId){
        final Project project = projectService.findById(projectId);
        final Member member = memberService.findById(userId);
        final ProjectParticipantsId participantsId = new ProjectParticipantsId(project, member);
        final boolean exists = projectParticipantsRepository.existsById(participantsId);

        if(!exists){
            throw new NotParticipantsException("프로젝트 참여자가 아닙니다.");
        }
    }

}
