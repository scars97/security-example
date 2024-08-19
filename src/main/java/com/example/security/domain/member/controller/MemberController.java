package com.example.security.domain.member.controller;

import com.example.security.domain.member.entity.MemberDetails;
import com.example.security.domain.member.service.MemberService;
import com.example.security.domain.member.dto.MemberListResponseDto;
import com.example.security.domain.member.dto.SignUpDto;
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
    public ResponseEntity<Long> signUp(@RequestBody SignUpDto dto) {
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
        log.info(request.getSession().getAttribute("SPRING_SECURITY_CONTEXT"));
        log.info(userDetails.getUsername());
        return ResponseEntity.ok("지속 인증");
    }
}
