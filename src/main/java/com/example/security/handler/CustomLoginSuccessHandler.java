package com.example.security.handler;

import com.example.security.domain.member.entity.Member;
import com.example.security.domain.member.entity.MemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.print.attribute.standard.Media;
import java.io.IOException;

@Log4j2
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    /**
     * 인증에 성공하여 반환된 Authentication 객체를 SecurityContextHolder의 context에 저장
     * 사용자의 정보를 꺼낼 경우 SecurityContextHolder의 context에서 조회한다.
     * @param request
     * @param response
     * @param chain
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        final Member member = ((MemberDetails) authentication.getPrincipal()).getMember();

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
