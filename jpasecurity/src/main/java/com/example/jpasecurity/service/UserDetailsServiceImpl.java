package com.example.jpasecurity.service;

import com.example.jpasecurity.service.UserAccount;
import com.example.jpasecurity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 로그인 시 Spring Security가 호출하는 클래스
// loadUserByUsername()이 반환한 UserAccount를 기준으로 비밀번호를 검증함

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return memberRepository.findByUsername(username)
                .map(UserAccount::new)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다: " +
                        username));
    }
}