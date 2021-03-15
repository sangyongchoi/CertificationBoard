package com.example.certificationboard.member.application;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import com.example.certificationboard.member.exception.DuplicateUserException;
import org.springframework.stereotype.Service;

import static com.example.certificationboard.member.exception.DuplicateUserException.DUPLICATED_USER_ID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String signUp(Member member) {
        if (isExistsMember(member)) {
            throw new DuplicateUserException(DUPLICATED_USER_ID);
        }

        final Member savedUser = memberRepository.save(member);
        return savedUser.getId();
    }

    public boolean isExistsMember(Member member) {
        return memberRepository.findById(member.getId()).isPresent();
    }
}
