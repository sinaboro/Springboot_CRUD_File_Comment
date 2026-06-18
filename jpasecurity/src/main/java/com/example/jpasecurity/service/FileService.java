package com.example.jpasecurity.service;

import com.example.jpasecurity.entity.Board;
import com.example.jpasecurity.entity.FileAttach;
import com.example.jpasecurity.repository.FileAttachRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final FileAttachRepository fileAttachRepository;

    // application.yml의 file.upload-dir 값을 주입받음
    @Value("${file.upload-dir}")
    private String uploadDir;

    //파일 저장 - 게시글 저장 후 파일 저장 호출
    public void saveFiles(List<MultipartFile> files, Board board) throws IOException {

        if(files == null || files.isEmpty()) return;

        for(var file : files){

            if(file.isEmpty()) continue;

            String savedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path savedPath = Paths.get(uploadDir, savedName);


            log.info("savedName : {}", savedName);
            log.info("originalName: {}", file.getOriginalFilename());
            log.info("uploadDir : {}", uploadDir);

            /*
                실제 파일을 디스크에 저장
                MutilpartFile로 업로드된 파일을 savedPath 위치에 저장한다.
                만약, 같은 이름의 파일이 존재하면 기존 파일을 덥어쓴다.
            */
            Files.copy(file.getInputStream(), savedPath, StandardCopyOption.REPLACE_EXISTING);

            // 파일 정보를 DB에 저장
            FileAttach attach = FileAttach.builder()
                    .originalName(file.getOriginalFilename())
                    .savedName(savedName)
                    .fileSize(file.getSize())
                    .board(board)
                    .build();

            fileAttachRepository.save(attach);
        }
    } //end saveFiles

    //파일 다운로드
    public Resource loadFileAsResource(String savedName)throws MalformedURLException {

        // uploadDir(업로드 폴더)와 savedName(저장된 파일명)을 합쳐 파일의 전체 경로(Path 객체)를 생성
        // 예) uploadDir = "c:/uploads"
        //     savedName = "cat.jpg"
        //     결과 → c:/uploads/cat.jpg
        Path filePath = Paths.get(uploadDir, savedName);


        // Path 객체를 URI로 변환한 후 UrlResource 객체 생성
        // Resource는 Spring에서 파일, 이미지, URL 등을 추상화한 인터페이스
        // 브라우저에 파일을 다운로드하거나 이미지를 출력할 때 많이 사용
        Resource resource = new UrlResource(filePath.toUri());


        // 해당 파일이 실제 존재하는지 확인
        // 존재하지 않으면 RuntimeException 예외 발생
        // 예) c:/uploads/cat.jpg 파일이 없으면 예외 발생
        if(!resource.exists())
            throw new RuntimeException("해당 파일을 찾을 수 없습니다.");


        // Resource 객체를 반환
        // Controller에서 반환하면 Spring이 파일을 읽어서 브라우저에 전송
        return resource;
    }  //end loadFileAsResource

    //파일 삭제
    public void deleteFile(Long fileId) throws  IOException{

        FileAttach attach = fileAttachRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("해당 파일 없음."));

        Path filePath = Paths.get(uploadDir, attach.getSavedName());

        Files.deleteIfExists(filePath); //디스크에서 파일 삭제

        fileAttachRepository.delete(attach); //DB 파일 삭제

    }
}
