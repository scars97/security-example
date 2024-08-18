package com.example.security.domain.member.service;

import com.example.security.domain.member.dto.MemberListResponseDto;
import com.example.security.domain.member.dto.SignUpDto;
import com.example.security.domain.member.entity.Member;
import com.example.security.domain.member.enums.MemberRole;
import com.example.security.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    
    public Member signUp(final SignUpDto dto) {
        final Member member = Member.builder()
                .memberId(dto.getMemberId())
                .pwd(passwordEncoder.encode(dto.getPwd()))
                .role(MemberRole.ROLE_MEMBER)
                .isEnable(true)
                .build();

        return memberRepository.save(member);
    }

    public boolean isMemberIdDuplicated(final String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    public MemberListResponseDto findAll() {
        List<Member> memberList = memberRepository.findAll();

        return MemberListResponseDto.from(memberList);
    }
}
