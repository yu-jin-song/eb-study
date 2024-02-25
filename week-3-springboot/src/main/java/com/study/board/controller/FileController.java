package com.study.board.controller;

import com.study.board.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 파일 다운로드 기능을 제공하는 컨트롤러
 */
@Controller
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * 클라이언트로부터 파일 다운로드 요청을 받아 해당 파일을 제공하는 메서드
     *
     * @param fileName 다운로드 요청 받은 파일의 이름
     * @param filePath 파일이 저장된 서버 상의 경로
     * @param extension 파일의 확장자
     * @return 파일의 리소스를 포함하는 ResponseEntity 객체를 반환
     * @throws IOException 파일 접근에 실패하거나 파일을 읽는 도중 오류가 발생했을 때 발생
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam("fileName") String fileName,
            @RequestParam("filePath") String filePath,
            @RequestParam("ext") String extension
    ) throws IOException {
        // TODO : 서비스로 분리
        // fileService.downloadFile(fileName, filePath, extension);

        // 파일 이름과 경로 디코딩
        String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        String decodedFilePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);

        // 파일에 대한 리소스 참조 생성
        Path file = Paths.get(decodedFilePath).resolve(decodedFileName);
        Resource resource = new UrlResource(file.toUri());

        // 컨텐츠 타입을 결정
        String contentType = Files.probeContentType(file);
        if (Objects.isNull(contentType)) {
            // 파일 타입을 결정할 수 없는 경우 기본값 사용
           contentType = "application/octet-stream";
        }

        // 파일명 인코딩 변환
        String encodedFileName = new String(
                decodedFileName.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.ISO_8859_1);
        encodedFileName += "." + extension;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"")
                .body(resource);
    }
}
