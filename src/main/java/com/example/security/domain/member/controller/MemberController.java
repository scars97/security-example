package com.example.security.domain.member.controller;

import com.example.security.domain.member.MemberService;
import com.example.security.domain.member.dto.MemberListResponseDto;
import com.example.security.domain.member.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("")
    public ResponseEntity<Long> signUp(@RequestBody SignUpDto dto) {
        return memberService.isMemberIdDuplicated(dto.getMemberId())
                ? ResponseEntity.badRequest().build()
                : ResponseEntity.ok(memberService.signUp(dto).getId());
    }

    @GetMapping("")
    public ResponseEntity<MemberListResponseDto> findAll() {
        return ResponseEntity.ok(memberService.findAll());
    }
}
