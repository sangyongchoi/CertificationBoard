package com.example.certificationboard.member.domain;

import com.example.certificationboard.common.BaseTimeEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Member extends BaseTimeEntity {

    @Id
    private String id;
    private String password;
    private String name;
    private Boolean isCertified;

    public Member() {
    }

    public Member(String id, String password, String name, Boolean isCertified) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.isCertified = isCertified;
    }

    public String getId() {
        return id;
    }
}
