package com.example.certificationboard.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String> {
    List<Member> findByIdIn(Collection<String> ids);
}
