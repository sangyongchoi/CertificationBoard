package com.example.certificationboard.projectparticipants.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@NoArgsConstructor
public class ProjectParticipants {

    @EmbeddedId
    private ProjectParticipantsId projectParticipantsId;

    private Boolean isStatus;
    private boolean favorites;
    @Enumerated(EnumType.STRING)
    private Role role;

    public ProjectParticipants(ProjectParticipantsId projectParticipantsId, Role role, boolean favorites) {
        this.projectParticipantsId = projectParticipantsId;
        this.role = role;
        this.favorites = favorites;
        join();
    }

    public void exit(){
        isStatus = false;
    }

    public void join(){
        isStatus = true;
    }

    public void addFavorite(){
        this.favorites = true;
    }

    public enum Role{
        ADMIN
        , MEMBER
    }

    public ProjectParticipantsId getProjectParticipantsId() {
        return projectParticipantsId;
    }

    public Boolean getStatus() {
        return isStatus;
    }

    public boolean isFavorites() {
        return favorites;
    }

    public Role getRole() {
        return role;
    }
}
