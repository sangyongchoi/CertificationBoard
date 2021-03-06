package com.example.certificationboard.member.application;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import com.example.certificationboard.member.exception.DuplicateUserException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.certificationboard.member.exception.DuplicateUserException.DUPLICATED_USER_ID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberListResponse getMembers(Pageable pageable) {
        final Page<Member> members = memberRepository.findAll(pageable);
        final List<Member> content = members.getContent();
        final List<MemberResponse> memberList = content.stream()
                .map(member -> new MemberResponse(member.getId(), member.getName()))
                .collect(Collectors.toList());

        return new MemberListResponse(memberList, !members.isLast());
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

    public List<Member> getUsersInfo(Collection<String> usersId){
        return memberRepository.findByIdIn(usersId);
    }

    private boolean isExistsMember(Member member) {
        return memberRepository.findById(member.getId()).isPresent();
    }

    private Member encryptPassword(Member member) {
        final String encryptPassword = passwordEncoder.encode(member.getPassword());
        return new Member(member.getId(), encryptPassword, member.getName(), member.getVerified());
    }
}
