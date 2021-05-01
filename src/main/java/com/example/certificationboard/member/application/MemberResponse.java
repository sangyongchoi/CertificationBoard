package com.example.certificationboard.member.application;

public class MemberResponse {

    private String id;
    private String name;

    public MemberResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
