package com.example.certificationboard.member.application;

import com.example.certificationboard.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberRequestTest {

    @Test
    @DisplayName("MemberRequest => Member 객체변환 테스트")
    public void memberRequest_to_member_test() {
        MemberRequest request = new MemberRequest("test", "test", "test");
        Member member = request.toMemberEntity();

        assertEquals(member.getId(), request.getId());
    }

}