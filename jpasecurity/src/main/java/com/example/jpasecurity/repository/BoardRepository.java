package com.example.jpasecurity.repository;

import com.example.jpasecurity.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // ★ 제목 또는 내용으로 검색 + 페이징
    // @Query: 메서드 이름으로 표현하기 어려운 조건을 JPQL로 직접 작성
    // :keyword → @Param으로 전달받은 값
    // Page<Board>: 결과 데이터 + 전체 건수 + 페이지 수 등을 모두 포함
    @Query("SELECT b FROM Board b WHERE b.title LIKE %:keyword% " +
            "OR b.content LIKE %:keyword% ORDER BY b.id DESC")
    Page<Board> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // 전체 목록 페이징 (최신순)
    // Pageable만 추가하면 자동으로 페이징 처리됩니다
    Page<Board> findAllByOrderByIdDesc(Pageable pageable);
}