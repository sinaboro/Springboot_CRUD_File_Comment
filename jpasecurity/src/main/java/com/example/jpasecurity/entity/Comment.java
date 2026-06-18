package com.example.jpasecurity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Table(name = "jpa_comment")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 댓글 내용 — 최대 500자
    @Column(nullable = false, length = 500)
    private String content;

    // ★ 어떤 게시글의 댓글인지 — 다대일(N:1)
    // 댓글(N) : 게시글(1) → 한 게시글에 댓글 여러 개
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    // ★ 누가 작성한 댓글인지 — 다대일(N:1)
    // 댓글(N) : 회원(1) → 한 회원이 여러 댓글 작성 가능
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private JpaMember member;

    // 댓글 등록일
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}