package com.example.jpasecurity.service;

import com.example.jpasecurity.dto.RegisterDto;
import com.example.jpasecurity.entity.JpaMember;
import com.example.jpasecurity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // SecurityConfig에서 Bean 등록

    // 회원가입 — 비밀번호는 BCrypt로 암호화하여 저장
    @Transactional
    public JpaMember register(RegisterDto dto) {

        if (memberRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        JpaMember member = JpaMember.builder()
                .name(dto.getName())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role("ROLE_USER")
                .build();
        return memberRepository.save(member);
    }
}