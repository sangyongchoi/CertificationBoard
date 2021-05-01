package com.example.certificationboard.member.presentation;

import com.example.certificationboard.common.config.argument.resolver.AuthUser;
import com.example.certificationboard.member.application.MemberRequest;
import com.example.certificationboard.member.application.MemberResponse;
import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.member.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(@RequestBody @Valid MemberRequest member) {
        Member savedUser = memberService.signUp(member.toMemberEntity());
        String userId = savedUser.getId();
        String userName = savedUser.getName();

        return new ResponseEntity<>(new MemberResponse(userId, userName), HttpStatus.OK);
    }

    @GetMapping("/user-info")
    public ResponseEntity<MemberResponse> userInfo(@AuthUser Member member) {
        String userId = member.getId();
        String name = member.getName();

        return new ResponseEntity<>(new MemberResponse(userId, name), HttpStatus.OK);
    }
}
