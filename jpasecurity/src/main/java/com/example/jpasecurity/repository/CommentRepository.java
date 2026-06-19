package com.example.jpasecurity.repository;

import com.example.jpasecurity.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

     // 특정 게시글의 댓글 목록을 등록일 오름차순으로 조회
     // → 게시글 상세 화면에서 댓글 목록을
     List<Comment> findByBoardIdOrderByCreatedAtAsc(Long boardId);
}

/*
select *
from Comment
where board_id = 5
order by Created_At asc
 */