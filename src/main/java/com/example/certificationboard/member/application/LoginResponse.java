package com.example.certificationboard.member.application;

public class LoginResponse {

    private String organizationId;
    private String token;

    public LoginResponse(String organizationId, String token) {
        this.organizationId = organizationId;
        this.token = token;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public String getToken() {
        return token;
    }
}
