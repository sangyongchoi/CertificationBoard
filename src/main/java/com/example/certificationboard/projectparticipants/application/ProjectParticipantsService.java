package com.example.certificationboard.projectparticipants.application;

import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.project.application.ProjectInviteRequest;
import com.example.certificationboard.project.application.ProjectService;
import com.example.certificationboard.project.domain.Project;
import com.example.certificationboard.projectparticipants.domain.ProjectParticipants;
import com.example.certificationboard.projectparticipants.domain.ProjectParticipantsId;
import com.example.certificationboard.projectparticipants.domain.ProjectParticipantsRepository;
import com.example.certificationboard.projectparticipants.exception.NotParticipantsException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

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

    public ProjectParticipants join(ProjectParticipantsId participantsId, ProjectParticipants.Role role){
        ProjectParticipants participants = new ProjectParticipants(participantsId, role, false);
        return projectParticipantsRepository.save(participants);
    }

    public void invite(ProjectInviteRequest inviteRequest) {
        Long projectId = inviteRequest.getProjectId();
        final String inviterId = inviteRequest.getInviterId();

        validateParticipants(projectId, inviterId);

        final String inviteeId = inviteRequest.getInviteeId();
        final Project project = projectService.findById(projectId);
        final Member member = memberService.findById(inviteeId);
        ProjectParticipantsId participantId = new ProjectParticipantsId(project, member);

        join(participantId, ProjectParticipants.Role.MEMBER);
    }

    public List<ProjectParticipantsDto> getProjectParticipantsList(Long projectId){
        return projectParticipantsRepository.findParticipantList(projectId);
    }

    public ProjectParticipants addFavorite(ProjectParticipantsId participantsId){
        final ProjectParticipants projectParticipants = getParticipants(participantsId);

        projectParticipants.addFavorite();
        projectParticipantsRepository.save(projectParticipants);

        return projectParticipants;
    }

    public ProjectParticipants getParticipants(ProjectParticipantsId participantsId){
        return projectParticipantsRepository.findById(participantsId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 값입니다."));
    }

    public void validateParticipants(Long projectId, String userId){
        final Project project = projectService.findById(projectId);
        final Member member = memberService.findById(userId);
        final ProjectParticipantsId participantsId = new ProjectParticipantsId(project, member);
        final boolean exists = projectParticipantsRepository.existsById(participantsId);

        if(!exists){
            throw new NotParticipantsException("프로젝트 참여자가 아닙니다.");
        }
    }

    public List<Member> getUsersInfo(Collection<String> usersId){
        return memberService.getUsersInfo(usersId);
    }
}
