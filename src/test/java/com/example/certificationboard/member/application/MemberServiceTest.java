package com.example.certificationboard.member.application;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import com.example.certificationboard.member.exception.DuplicateUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    MemberService memberService;

    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup(){
        passwordEncoder = new BCryptPasswordEncoder();
        memberService = new MemberService(memberRepository, passwordEncoder);
        insertUser("getUsersTest");
    }

    void insertUser(String userId){
        Member member1 = new Member(userId, "test", "test", false);
        memberRepository.save(member1);
        memberRepository.flush();
    }

    @Test
    @DisplayName("다수의 유저 정보 가져오기 테스트")
    @Order(1)
    public void get_users_info() {
        // given
        Set<String> users = new HashSet<>();
        users.add("getUsersTest");

        // when
        final List<Member> usersInfo = memberService.getUsersInfo(users);

        //then
        assertEquals(1, usersInfo.size());
        assertEquals("getUsersTest", usersInfo.get(0).getId());
        assertEquals("test", usersInfo.get(0).getName());
    }

    @Test
    @DisplayName("회원가입 테스트 - 성공 (비밀번호 암호화)")
    @Order(2)
    public void signup_when_success() {
        // given
        String userId = "test";
        Member member = new Member(userId, "test", "test", false);

        // when
        Member savedUser = memberService.signUp(member);
        String savedUserId = savedUser.getId();
        // then
        assertEquals(userId, savedUserId);
        assertTrue(passwordEncoder.matches(member.getPassword(), savedUser.getPassword()));
    }

    @Test
    @DisplayName("회원가입 테스트 - 이미 유저가 존재할 때")
    @Order(3)
    public void signup_when_exists_user() {
        //when
        assertThrows(DuplicateUserException.class, () -> {
            // given
            String userId = "signup";
            insertUser(userId);
            Member member = new Member(userId, "test", "test", false);

            // then
            memberService.signUp(member);
        });
    }
}