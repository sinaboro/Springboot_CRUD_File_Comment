package com.example.jpasecurity.service;

import com.example.jpasecurity.dto.BoardDto;
import com.example.jpasecurity.entity.Board;
import com.example.jpasecurity.entity.JpaMember;
import com.example.jpasecurity.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileService fileService;

    // ★ 게시글 목록 조회 + 페이징
    // page: 현재 페이지 번호 (0부터 시작)
    // keyword: 검색어 (없으면 전체 조회)
    @Transactional(readOnly = true)
    public Page<Board> getList(int page, String keyword) {
        // PageRequest.of(페이지번호, 페이지당 건수, 정렬방식)
        // → page=0, size=10, 최신순(id 내림차순)
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        if (keyword != null && !keyword.isBlank()) {
            // 검색어가 있으면 제목·내용 검색
            return boardRepository.searchByKeyword(keyword, pageable);
        }
        // 검색어 없으면 전체 최신순 조회
        return boardRepository.findAllByOrderByIdDesc(pageable);
    }

    // 게시글 상세 조회 + 조회수 증가
    @Transactional
    public Board getDetail(Long id) {

        Board board = findById(id);

        board.increaseViewCount(); // Dirty Checking → save() 없이 UPDATE
        return board;
    }

    // 게시글 등록
    @Transactional
    public Board write(BoardDto dto, JpaMember member) throws IOException {
        Board board = Board.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .member(member) // 작성자 설정
                .build();

        boardRepository.save(board);

        // 파일이 있으면 저장
        if (dto.getFiles() != null) {
            fileService.saveFiles(dto.getFiles(), board);
        }
        return board;
    }

    // 단건 조회
    @Transactional(readOnly = true)
    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }

    // 게시글 수정
    @Transactional
    public void update(Long id, BoardDto dto) throws IOException {
        Board board = findById(id);

        board.update(dto.getTitle(), dto.getContent());
        // 추가 파일 업로드
        if (dto.getFiles() != null) {
            fileService.saveFiles(dto.getFiles(), board);
        }
    }

    // 게시글 삭제 (댓글·파일은 cascade로 자동 삭제)
    @Transactional
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
}