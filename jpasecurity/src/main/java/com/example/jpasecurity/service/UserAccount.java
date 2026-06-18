package com.example.jpasecurity.service;

import com.example.jpasecurity.entity.JpaMember;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

// Spring Security가 로그인 인증 시 사용하는 사용자 정보 객체
// getJpaMember()로 실제 회원 Entity를 꺼낼 수 있음 (★ 게시판/댓글 작성자 처리에 사용)
@Getter
public class UserAccount implements UserDetails {

    private final JpaMember jpaMember;

    public UserAccount(JpaMember jpaMember) {
        this.jpaMember = jpaMember;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(jpaMember.getRole()));
    }

    @Override
    public String getPassword() {
        return jpaMember.getPassword();
    }

    @Override
    public String getUsername() {
        return jpaMember.getUsername();
    }
}