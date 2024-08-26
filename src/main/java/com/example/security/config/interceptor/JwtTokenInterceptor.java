package com.example.security.config.interceptor;

import com.example.security.constants.AuthConstants;
import com.example.security.utils.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.servlet.HandlerInterceptor;

@Log4j2
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String header = request.getHeader(AuthConstants.AUTH_HEADER);

        if (header != null) {
            String token = JwtTokenUtils.getTokenFromHeader(header);
            if (JwtTokenUtils.isValidToken(token)) {
                return true;
            }
        }
         response.sendError(401, "인증 실패");
        return false;
    }
}
