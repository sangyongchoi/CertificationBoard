package com.example.certificationboard.member.domain;

import com.example.certificationboard.common.BaseTimeEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Member extends BaseTimeEntity {

    @Id
    private String id;
    private String password;
    private String name;
    private Boolean isVerified;
    @Enumerated(EnumType.STRING)
    private Role role;

    public Member() {
    }

    public Member(String id, String password, String name, Boolean isVerified) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.isVerified = isVerified;
        this.role = Role.ROLE_MEMBER;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public Role getRole() {
        return role;
    }
}
