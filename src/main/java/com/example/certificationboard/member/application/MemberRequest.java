package com.example.certificationboard.member.application;

import com.example.certificationboard.member.domain.Member;

import javax.validation.constraints.Size;

public class MemberRequest {

    @Size(min = 8)
    private String id;
    @Size(min = 8)
    private String password;
    private String name;

    public MemberRequest(String id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
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

    public Member toMemberEntity() {
        return new Member(this.id, this.password, this.name, false);
    }
}
