package com.example.security.domain.member.dto;

import com.example.security.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class MemberListResponseDto {

    private final List<Member> memberList;

    public static MemberListResponseDto from(List<Member> memberList) {
        return MemberListResponseDto.builder()
                .memberList(memberList)
                .build();
    }
}
