package com.example.certificationboard.member.domain;

import com.example.certificationboard.common.BaseTimeEntity;
import com.example.certificationboard.common.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTimeEntity implements UserDetails {

    @Id
    private String id;
    private String password;
    private String name;
    private Boolean isVerified;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String organizationId;

    public Member(String id, String password, String name, Boolean isVerified) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.isVerified = isVerified;
        this.role = Role.ROLE_MEMBER;
        this.organizationId = UUID.randomUUID() + DateUtil.nowToString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(role.name()));

        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum Role {
        ROLE_ADMIN,
        ROLE_MEMBER
    }

}
