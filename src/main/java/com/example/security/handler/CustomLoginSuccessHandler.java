package com.example.security.handler;

import com.example.security.constants.AuthConstants;
import com.example.security.domain.entity.Member;
import com.example.security.domain.entity.MemberDetails;
import com.example.security.utils.JwtTokenUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Log4j2
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("토큰 발급 진행");

        final Member member = ((MemberDetails) authentication.getPrincipal()).getMember();

        final String token = JwtTokenUtils.generateJwtToken(member);

        log.info("로그인 성공");

        response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + token);

        // form 로그인 시 동작 설정
        /*response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        log.info(SecurityContextHolder.getContext().getAuthentication());
        response.getWriter().println("success");*/
    }
}
