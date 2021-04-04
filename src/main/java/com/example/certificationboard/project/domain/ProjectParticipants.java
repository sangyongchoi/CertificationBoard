package com.example.certificationboard.project.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@NoArgsConstructor
public class ProjectParticipants {

    @EmbeddedId
    private ProjectParticipantsId projectParticipantsId;

    private Boolean isStatus;

    @Enumerated(EnumType.STRING)
    private Role role;

    public ProjectParticipants(ProjectParticipantsId projectParticipantsId, Role role) {
        this.projectParticipantsId = projectParticipantsId;
        this.role = role;
        join();
    }

    public void exit(){
        isStatus = false;
    }

    public void join(){
        isStatus = true;
    }

    public enum Role{
        ADMIN
        , MEMBER
    }
}