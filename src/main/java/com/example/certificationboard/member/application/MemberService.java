package com.example.certificationboard.member.application;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import com.example.certificationboard.member.exception.DuplicateUserException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.certificationboard.member.exception.DuplicateUserException.DUPLICATED_USER_ID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member findById(String id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    public Member signUp(Member member) {
        if (isExistsMember(member)) {
            throw new DuplicateUserException(DUPLICATED_USER_ID);
        }

        return memberRepository.save(encryptPassword(member));
    }

    private boolean isExistsMember(Member member) {
        return memberRepository.findById(member.getId()).isPresent();
    }

    private Member encryptPassword(Member member) {
        final String encryptPassword = passwordEncoder.encode(member.getPassword());
        return new Member(member.getId(), encryptPassword, member.getName(), member.getVerified());
    }
}
