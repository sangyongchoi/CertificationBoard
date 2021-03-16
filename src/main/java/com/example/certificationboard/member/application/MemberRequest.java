package com.example.certificationboard.member.application;

import com.example.certificationboard.member.domain.Member;

import javax.validation.constraints.NotBlank;

public class MemberRequest {

    @NotBlank(message = "ID를 입력해주세요.")
    private String id;
    @NotBlank(message = "비밀번호를 입력해주세요.")
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
        return new Member(this.id, this.password, this.name);
    }
}
