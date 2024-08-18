package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * 애플리케이션 보안 설정 정의
     * SecurityFilterChain으로 들어오는 요청을 필터링하고, 권한 부여 처리
     * http 요청을 가로채고, 보안 관련 로직을 적용한다.
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // POST, PUT, DELETE 같은 상태 변경을 하는 HTTP 메서드를 사용할 때,
        // CSRF 토큰이 없거나 올바르지 않으면 403 Forbidden 에러가 발생
        http.csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/members/**").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    /**
     * 사용자의 정보를 검증하여, 시스템에 접근할 수 있는 권한이 있는지 확인
     * @param userDetailsService
     * @param passwordEncoder
     * @return
     *//*
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        // DB 또는 메모리 내의 사용자 정보를 사용하여 인증 처리
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }*/

    /**
     * 사용자 비밀번호 암호화
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
