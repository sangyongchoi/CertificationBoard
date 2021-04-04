package com.example.certificationboard.project.domain;

import com.example.certificationboard.common.BaseTimeEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
}
