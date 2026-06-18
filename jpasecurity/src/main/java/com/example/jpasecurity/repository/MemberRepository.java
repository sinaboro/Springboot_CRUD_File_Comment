package com.example.jpasecurity.repository;

import com.example.jpasecurity.entity.JpaMember;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<JpaMember, Long> {
    // 로그인 ID로 회원 조회 — 로그인 인증에 사용
    Optional<JpaMember> findByUsername(String username);
    // 아이디 중복 확인
    boolean existsByUsername(String username);
}