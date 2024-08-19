package com.example.security.config.security;

import com.example.security.domain.member.entity.Member;
import com.example.security.exception.InputNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/**
 * 사용자가 로그인을 요청할 때 JSON 형식의 데이터를 파싱하여 인증 처리
 */
@Log4j2
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 사용자가 인증을 시도할 때 호출.
     * HTTP 요청과 응답 객체를 받아 인증을 처리한다.
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 사용자의 인증 정보를 담는 컨테이너
        final UsernamePasswordAuthenticationToken authRequest;
        try {
            final Member member = new ObjectMapper().readValue(request.getInputStream(), Member.class);
            // 사용자 정보를 포함하는 토큰 객체 생성
            authRequest = new UsernamePasswordAuthenticationToken(member.getMemberId(), member.getPwd());
        } catch (IOException e) {
            throw new InputNotFoundException();
        }

        // 추가적인 요청 정보 설정 - IP 주소, 세션 ID 등
        setDetails(request, authRequest);
        // 생성된 토큰을 사용하여 AuthenticationManager 를 통해 실제 인증 수행
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
