package com.example.jpasecurity.repository;

import com.example.jpasecurity.entity.Board;
import com.example.jpasecurity.entity.Comment;
import com.example.jpasecurity.entity.JpaMember;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertTest(){

        Board board = boardRepository.findById(141L)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID 미존재"));
        JpaMember member = memberRepository.findById(3L)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID 미존재"));

        for(int i=0; i<10; i++){
            Comment comment = Comment.builder()
                    .board(board)
                    .member(member)
                    .content("댓글 내용 작성 " + i)
                    .createdAt(LocalDateTime.now())
                    .build();

            commentRepository.save(comment);
        }
    } //end insertTest

    @Test
    public void searchTest(){
        commentRepository.findByBoardIdOrderByCreatedAtAsc(141L)
                .forEach(comment -> log.info("comment : {}", comment.getContent()));
    }

}