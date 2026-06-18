package com.example.jpasecurity.repository;

import com.example.jpasecurity.entity.FileAttach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileAttachRepository extends JpaRepository<FileAttach, Long> {

    // 특정 게시글의 첨부파일 목록 조회
    List<FileAttach> findByBoardId(Long boardId);
}
