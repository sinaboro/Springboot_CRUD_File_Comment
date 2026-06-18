package com.example.jpasecurity.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jpa_member")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JpaMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 화면에 표시할 이름
    @Column(nullable = false, length = 50)
    private String name;

    // 로그인 ID — 중복 불가
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // 비밀번호 — BCrypt로 암호화된 값이 저장됨
    @Column(nullable = false)
    private String password;

    // 권한 — 예: ROLE_USER, ROLE_ADMIN
    @Column(nullable = false, length = 30)
    @Builder.Default
    private String role = "ROLE_USER";
}