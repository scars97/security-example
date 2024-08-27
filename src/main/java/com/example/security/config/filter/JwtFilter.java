package com.example.security.config.filter;

import com.example.security.constants.AuthConstants;
import com.example.security.domain.entity.Member;
import com.example.security.domain.entity.MemberDetails;
import com.example.security.domain.enums.MemberRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.example.security.utils.JwtTokenUtils.*;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader(AuthConstants.AUTH_HEADER);

        if (header != null) {
            String token = getTokenFromHeader(header);
            if (isValidToken(token)) {
                String memberId = getMemberIdFromToken(token);


                Member member = Member.builder()
                        .memberId(memberId)
                        .pwd("")
                        .role(MemberRole.ROLE_MEMBER)
                        .build();
                MemberDetails memberDetails = new MemberDetails(member);

                Authentication auth =  new UsernamePasswordAuthenticationToken(memberDetails, "", memberDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
