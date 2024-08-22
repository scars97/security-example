package com.example.security.config.security;

import com.example.security.handler.CustomLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;

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
        http.csrf(AbstractHttpConfigurer::disable); // csrf 비활성화

        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/", "/login", "/signUp", "/login-proc", "/signUp-proc").permitAll() // 누구나 접근 가능
                        .requestMatchers("/admin").hasRole("ADMIN") // admin 권한을 가진 경우 접근 가능
                        .anyRequest().authenticated() // 그 외 경로는 로그인 인증 후 접근 가능
                );

        http
                .formLogin(authorize -> authorize
                        .loginPage("/login")
                        .loginProcessingUrl("/login-proc")
                        .permitAll()
                );

        http
                .sessionManagement(session -> session
                        .maximumSessions(1) // 하나의 아이디에 대한 다중 로그인 허용 개수
                        .maxSessionsPreventsLogin(true) // 다중 로그인 개수 초과 시 처리 방법 -> true: 새로운 로그인 차단 / false: 기존 세션 삭제
                );

        // 세션 고정 공격 보호
        http
                .sessionManagement(session -> session
                        //.sessionFixation().changeSessionId() // 로그인 시 동일한 세션에 대한 id 변경
                        .sessionFixation().newSession() // 로그인 시 세션 새로 생성
                        //.sessionFixation().none() // 로그인 시 세션 정보 변경 x
                );

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
        customAuthenticationFilter.setFilterProcessesUrl("/login-proc");
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler());
        customAuthenticationFilter.afterPropertiesSet();
        // Persisting Authentication 지속 인증 설정
        customAuthenticationFilter.setSecurityContextRepository(new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()));
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
