package com.example.jpasecurity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
// 게시글 등록·수정 폼 데이터를 담는 DTO
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {

    // 제목 — 필수, 최대 200자
    @NotBlank(message = "제목을 입력해주세요")
    @Size(max = 200, message = "제목은 200자 이내로 입력해주세요")
    private String title;

    // 내용 — 필수
    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    // ★ 파일 첨부 — MultipartFile: HTTP 멀티파트 요청에서 파일을 받는 타입
    // required = false 처리됨 → 파일 없이도 등록 가능
    // List: 여러 파일 동시 업로드 가능
    private List<MultipartFile> files;
}