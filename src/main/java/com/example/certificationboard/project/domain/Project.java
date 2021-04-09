package com.example.certificationboard.project.domain;

import com.example.certificationboard.common.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String organizationId;
    private String createdMemberId;
    private String title;
    private String explain;
    private boolean favorites;

    public Project() {
    }

    public Project(String organizationId, String createdMemberId, String title, String explain) {
        this.organizationId = organizationId;
        this.createdMemberId = createdMemberId;
        this.title = title;
        this.explain = explain;
        favorites = false;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", organizationId='" + organizationId + '\'' +
                ", createdMemberId='" + createdMemberId + '\'' +
                ", title='" + title + '\'' +
                ", explain='" + explain + '\'' +
                ", favorites=" + favorites +
                '}';
    }
}
