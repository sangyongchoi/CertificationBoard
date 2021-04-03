package com.example.certificationboard.project.domain;

import com.example.certificationboard.common.BaseTimeEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Project extends BaseTimeEntity {

    @Id
    private Long id;
    private String organizationId;
    private String createdMemberId;
}
