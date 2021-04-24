package com.example.certificationboard.project.domain;

import com.example.certificationboard.common.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String organizationId;
    private String createdMemberId;
    private String title;
    private String explain;

    public Project() {
    }

    public Project(String organizationId, String createdMemberId, String title, String explain) {
        this.organizationId = organizationId;
        this.createdMemberId = createdMemberId;
        this.title = title;
        this.explain = explain;
    }

    public Long getId() {
        return id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getCreatedMemberId() {
        return createdMemberId;
    }

    public String getTitle() {
        return title;
    }

    public String getExplain() {
        return explain;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", organizationId='" + organizationId + '\'' +
                ", createdMemberId='" + createdMemberId + '\'' +
                ", title='" + title + '\'' +
                ", explain='" + explain + '\'' +
                '}';
    }

}
