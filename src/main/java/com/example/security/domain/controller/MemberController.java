package com.example.security.domain.controller;

import com.example.security.domain.dto.MemberListResponseDto;
import com.example.security.domain.dto.MemberDto;
import com.example.security.domain.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
@Log4j2
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseEntity<Long> signUp(@RequestBody MemberDto dto) {
        return memberService.isMemberIdDuplicated(dto.getMemberId())
                ? ResponseEntity.badRequest().build()
                : ResponseEntity.ok(memberService.signUp(dto).getId());
    }

    @GetMapping("/list")
    public ResponseEntity<MemberListResponseDto> findAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

    @GetMapping("")
    public ResponseEntity<String> loginSuccess(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        log.info(SecurityContextHolder.getContext().getAuthentication());
        log.info(userDetails.getUsername());

        return ResponseEntity.ok("지속 인증");
    }
}
