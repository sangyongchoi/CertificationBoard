package com.example.certificationboard.member.presentation;

import com.example.certificationboard.member.application.MemberRequest;
import com.example.certificationboard.member.application.MemberResponse;
import com.example.certificationboard.member.application.MemberService;
import com.example.certificationboard.member.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signup(@RequestBody MemberRequest member) {
        final String savedUserId = memberService.signUp(member.toMemberEntity());

        return new ResponseEntity<>(new MemberResponse(savedUserId), HttpStatus.OK);
    }
}
