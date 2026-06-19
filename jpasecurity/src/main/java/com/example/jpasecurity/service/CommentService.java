package com.example.jpasecurity.service;

import com.example.jpasecurity.entity.Board;
import com.example.jpasecurity.entity.Comment;
import com.example.jpasecurity.entity.JpaMember;
import com.example.jpasecurity.repository.BoardRepository;
import com.example.jpasecurity.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    //댓글 등록
    @Transactional
    public void write(Long boardId, String content, JpaMember member){

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        Comment comment = Comment.builder()
                .content(content)
                .board(board)    //게시글
                .member(member)  //댓글 작성자
                .build();

        commentRepository.save(comment);
    }

    //게시글 댓글 조회
    @Transactional(readOnly = true)
    public List<Comment> getComments(Long boardId){
        return  commentRepository.findByBoardIdOrderByCreatedAtAsc(boardId);
    }

    //댓글 ID 조회
    @Transactional(readOnly = true)
    public Comment findById(Long id){
        return commentRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
    }

    //댓글 삭제
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
