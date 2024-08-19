package com.example.security.config.security;

import com.example.security.handler.CustomLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

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
                        .requestMatchers("/members/signUp", "/members/login").permitAll()
                        .anyRequest().authenticated()
                );
                /*.formLogin(login -> login.loginPage("/login")
                        .loginProcessingUrl("/members/login")
                        .defaultSuccessUrl("/members/test"));*/

        return http.build();
    }

    /**
     * 사용자 비밀번호 암호화
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 인증 플로우
     * @return
     */
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
        customAuthenticationFilter.setFilterProcessesUrl("/members/login");
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }

    /**
     * 사용자의 정보를 검증하여, 시스템에 접근할 수 있는 권한이 있는지 확인
     * @return
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        // AuthenticationProvider를 조회하여 인증 요구
        return new ProviderManager(customAuthenticationProvider());
    }

    /**
     * 입력 받은 사용자 정보 조회 후, 토큰 반환
     * @return
     */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    /**
     * 인증 성공 시 수행되는 Handler
     * 반환된 Authentication 객체를 SecurityContextHolder에 저장
     * @return
     */
    @Bean
    public CustomLoginSuccessHandler customLoginSuccessHandler() {
        return new CustomLoginSuccessHandler();
    }
}
