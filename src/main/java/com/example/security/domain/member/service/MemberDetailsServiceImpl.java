package com.example.security.domain.member.service;

import com.example.security.domain.member.entity.MemberDetails;
import com.example.security.domain.member.repository.MemberRepository;
import com.example.security.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public MemberDetails loadUserByUsername(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .map(MemberDetails::new)
                .orElseThrow(() -> new MemberNotFoundException(memberId));
    }
}
