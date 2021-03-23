package com.example.certificationboard.security.authentication;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public UserDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        final Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));

        return User.builder()
                .username(member.getId())
                .password(member.getPassword())
                .build();
    }
}
