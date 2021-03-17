package com.example.certificationboard.member.application;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import com.example.certificationboard.member.exception.DuplicateUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    MemberService memberService;

    @BeforeEach
    void setup(){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberService = new MemberService(memberRepository, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트 - 성공")
    public void signup_when_success() {
        // given
        String userId = "test";
        Member member = new Member(userId, "test", "test", false);

        // when
        Member savedUser = memberService.signUp(member);
        String savedUserId = savedUser.getId();
        // then
        assertEquals(userId, savedUserId);
    }

    @Test
    @DisplayName("회원가입 테스트 - 이미 유저가 존재할 때")
    public void signup_when_exists_user() {
        //when
        assertThrows(DuplicateUserException.class, () -> {
            // given
            String userId = "signup";
            insertUser(userId);
            Member member = new Member(userId, "test", "test", false);

            // when
            memberService.signUp(member);
        });
    }

    @Test
    @DisplayName("중복 ID 테스트 - 존재할 때")
    public void duplicate_user_exists() {
        // given
        final String userId = "test1";
        insertUser(userId);
        Member member = new Member(userId, "test", "test", false);

        // when
        boolean isExists = memberService.isExistsMember(member);

        // then
        assertTrue(isExists);
    }

    @Test
    @DisplayName("중복 ID 테스트 - 존재하지 않을 때")
    public void duplicate_user_when_not_exists() {
        // given
        Member member1 = new Member("test2", "test", "test", false);

        // when
        boolean isExists = memberService.isExistsMember(member1);

        // then
        assertFalse(isExists);
    }

    @Test
    @DisplayName("비밀번호 암호화 테스트")
    public void password_crypt_success() {
        // given
        Member member = new Member("csytest1", "csytest1", "1111", false);
        // when
        //Member cryptMember = memberService.encrytePassword(member);
        //then
    }

    void insertUser(String userId){
        Member member1 = new Member(userId, "test", "test", false);
        memberRepository.save(member1);
        memberRepository.flush();
    }
}