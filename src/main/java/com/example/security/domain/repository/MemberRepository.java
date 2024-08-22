package com.example.security.domain.repository;

import com.example.security.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByMemberId(String memberId);

    Optional<Member> findByMemberId(String memberId);
}
