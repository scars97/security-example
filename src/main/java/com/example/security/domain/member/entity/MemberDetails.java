package com.example.security.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Optional;

/**
 * 스프링 시큐리티가 사용자 인증, 권한 관리를 수행할 때 필요한 정보를 제공하는 클래스
 * 이 클래스는 사용자의 정보를 기반으로 스프링 시큐리티와 통합된다.
 */
@RequiredArgsConstructor
@Getter
public class MemberDetails implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(member).stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().getValue()))
                .toList();
    }

    @Override
    public String getPassword() {
        return member.getPwd();
    }

    @Override
    public String getUsername() {
        return member.getMemberId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return member.getIsEnable();
    }

    @Override
    public boolean isAccountNonLocked() {
        return member.getIsEnable();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return member.getIsEnable();
    }

    @Override
    public boolean isEnabled() {
        return member.getIsEnable();
    }
}
