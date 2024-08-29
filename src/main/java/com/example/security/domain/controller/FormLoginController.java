package com.example.security.domain.controller;

import com.example.security.domain.dto.MemberDto;
import com.example.security.domain.entity.MemberDetails;
import com.example.security.domain.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Log4j2
@Controller
@RequiredArgsConstructor
public class FormLoginController {

    private final MemberService memberService;

    @GetMapping("/")
    public String main(Model model, HttpServletRequest request, @AuthenticationPrincipal MemberDetails memberDetails) {
        model.addAttribute("user", memberDetails.getUsername());
        log.info(SecurityContextHolder.getContext().getAuthentication());
        log.info(request.getSession().getAttribute("SPRING_SECURITY_CONTEXT"));

        return "main";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signUp")
    public String singUp(Model model) {
        model.addAttribute("signUpForm", new MemberDto());
        return "signUp";
    }

    @PostMapping("/signUp-proc")
    public String signUpProcess(MemberDto dto) {
        boolean memberIdDuplicated = memberService.isMemberIdDuplicated(dto.getMemberId());

        if (memberIdDuplicated) {
            log.info("중복된 아이디명이 존재합니다.");
        } else {
            memberService.signUp(dto);
        }

        return memberIdDuplicated ? "redirect:/signUp" : "redirect:/login";
    }
}
