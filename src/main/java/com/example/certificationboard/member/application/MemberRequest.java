package com.example.certificationboard.member.application;

import com.example.certificationboard.member.domain.Member;

public class MemberRequest {

    private String id;
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

    public Member toMemberEntity() {
        return new Member(this.id, this.password, this.name);
    }
}
