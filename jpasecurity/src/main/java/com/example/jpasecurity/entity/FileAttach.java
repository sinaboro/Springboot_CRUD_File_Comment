package com.example.jpasecurity.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "jpa_file_attach")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileAttach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 원본 파일명 — 사용자가 업로드한 파일 이름
    @Column(nullable = false)
    private String originalName;

    // 저장 파일명 — UUID로 생성한 고유 이름 (파일명 충돌 방지)
    // 예: 3f2a1b4c-... .jpg
    @Column(nullable = false)
    private String savedName;

    // 파일 크기 (bytes)
    private Long fileSize;

    // ★ 어떤 게시글의 첨부파일인지 — 다대일(N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
}