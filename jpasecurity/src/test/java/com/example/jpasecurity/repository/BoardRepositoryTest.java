package com.example.jpasecurity.repository;

import com.example.jpasecurity.entity.Board;
import com.example.jpasecurity.entity.JpaMember;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    //=========================================
    // 더미 데이터 100개 생성
    //=========================================
    @Test
    @DisplayName("Board 더미 데이터 100개 생성")
    void insertDummyBoards(){

        JpaMember member =
                memberRepository.findById(1L)
                        .orElseThrow();

        for(int i=1;i<=100;i++){

            Board board = Board.builder()
                    .title("게시글 제목 " + i)
                    .content("게시글 내용입니다.... " + i)
                    .member(member)
                    .build();

            boardRepository.save(board);

            System.out.println("저장 : " + board.getId());
        }
    } //end insertDummyBoards

    @Test
    @DisplayName("전체 목록 페이징")
    public void paging_test(){

        Pageable pageable = PageRequest.of(1, 10);

        Page<Board> result = boardRepository.findAllByOrderByIdDesc(pageable);

        log.info("전체건수 : {}", result.getTotalElements());
        log.info("전페이이지 : {}", result.getTotalPages());
        log.info("현재 페이지 : {}", result.getNumber());

        result.getContent()
                .forEach(board-> log.info("{}", board));
    }  //end paging_test

    @Test
    @DisplayName("제목 또는 내용 검색")
    void search_test(){

        Pageable pageable =
                PageRequest.of(0, 10,Sort.by("id").descending());


        Page<Board> result =
                boardRepository.searchByKeyword(
                        "8",
                        pageable
                );

        System.out.println("전체건수 : " + result.getTotalElements());

        result.getContent().forEach(board->{

            System.out.println(board.getId());
            System.out.println(board.getTitle());
            System.out.println(board.getContent());

        });

    }

} //end BoardRepositoryTest