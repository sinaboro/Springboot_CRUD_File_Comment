package com.example.jpasecurity.controller;

import com.example.jpasecurity.entity.FileAttach;
import com.example.jpasecurity.repository.FileAttachRepository;
import com.example.jpasecurity.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;
    private final FileAttachRepository fileAttachRepository;

    //파일 다운로드
    @GetMapping("/files/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable Long fileId) throws  Exception{

        FileAttach attach = fileAttachRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("해당 파일 없음"));

        //디스크에서 파일을 Resource로 로드
        Resource resource = fileService.loadFileAsResource(attach.getSavedName());

        //한글 파일명이 끼지지 않도록 URL 인코딩
        String encodedName =
                URLEncoder.encode(attach.getSavedName(), StandardCharsets.UTF_8).replace("+", "%20");

        /*
         HTTP 응답 상태코드 200(OK) 생성
         파일 다운로드 응답을 만들기 위한 ResponseEntity 시작
            return ResponseEntity.ok()

         응답 헤더 추가
         Content-Disposition은 브라우저에게
         "이 파일은 화면에 출력하지 말고 다운로드하라"고 알려줌

         attachment          : 다운로드 창 표시
         filename*=UTF-8''   : 한글 파일명을 UTF-8로 인코딩하여 전달
         encodedName         : 인코딩된 파일명

         예)
         attachment; filename*=UTF-8''%ED%8C%8C%EC%9D%BC.jpg

         브라우저 다운로드 창에는
         "파일.jpg" 로 표시됨
        .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename*=UTF-8''" + encodedName
        )

         파일의 MIME 타입 설정
         application/octet-stream 은
         "모든 종류의 바이너리 파일"이라는 의미

         브라우저는 이 타입을 받으면
         보통 다운로드 창을 띄움

         예)
         pdf
         zip
         exe
         jpg
         hwp
         모두 다운로드 가능
        .contentType(MediaType.APPLICATION_OCTET_STREAM)

         실제 다운로드할 파일(Resource 객체)을 응답 본문에 담음
        .body(resource);
         */

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename*=UTF-8''" + encodedName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    } // download

}
