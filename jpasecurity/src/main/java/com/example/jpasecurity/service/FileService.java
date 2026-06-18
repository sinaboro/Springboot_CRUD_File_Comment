package com.example.jpasecurity.service;

import com.example.jpasecurity.entity.Board;
import com.example.jpasecurity.entity.FileAttach;
import com.example.jpasecurity.repository.FileAttachRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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


    }
}
