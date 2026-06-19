package com.example.jpasecurity.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// @EntityListeners: JPA Auditing 기능 활성화
// → @CreatedDate, @LastModifiedDate 어노테이션이 자동으로 날짜를 채워줍니다
// ★ JpaSecurityApplication.java에 @EnableJpaAuditing 추가 필요
@Entity
@Table(name = "jpa_board")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 제목 — 필수, 최대 200자
    @Column(nullable = false, length = 200)
    private String title;

    // 내용 — @Lob: 대용량 텍스트 (MySQL: TEXT 타입으로 생성)
    @Lob
    @Column(nullable = false)
    private String content;

    // 조회수 — 기본값 0
    @Column(nullable = false)
    @Builder.Default
    private int viewCount = 0;

    // ★ 작성자 연관관계 — 다대일(N:1)
    // 게시글(N) : 회원(1) → 여러 게시글이 한 회원에 속함
    // @ManyToOne: 다대일 관계 선언
    // fetch = LAZY: 지연 로딩 (게시글 조회 시 회원 정보를 즉시 가져오지 않음 → 성능 최적화)
    // @JoinColumn: 외래키 컬럼명 지정 (jpa_board 테이블에 member_id 컬럼 생성)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private JpaMember member;

    // 댓글 목록 — 일대다(1:N) 양방향
    // mappedBy: Comment 클래스의 'board' 필드가 연관관계 주인임을 선언
    // cascade = REMOVE: 게시글 삭제 시 댓글도 자동 삭제
    // orphanRemoval: 댓글이 게시글에서 분리되면 자동 삭제
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    // 파일첨부 목록 — 일대다(1:N)
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<FileAttach> files = new ArrayList<>();

    // 등록일 — @CreatedDate: 최초 저장 시 자동으로 현재 시간 입력
    @CreatedDate
    @Column(updatable = false) // 등록 후 수정 불가
    private LocalDateTime createdAt;

    // 수정일 — @LastModifiedDate: 수정 시마다 자동으로 현재 시간 갱신
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 게시글 수정 메서드 (title, content만 변경 가능)
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // 조회수 증가 메서드 (조회 시 호출)
    public void increaseViewCount() {
        this.viewCount++;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", viewCount=" + viewCount +
                '}';
    }
}