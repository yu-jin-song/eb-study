package com.study.board.controller;

import com.study.board.dto.FileDTO;
import com.study.board.service.FileService;
import com.study.board.util.FileUtil;
import com.study.board.util.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 파일 관련 기능을 제공하는 컨트롤러
 */
@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;
    private final FileUtil fileUtil;

    /**
     * 클라이언트로부터 파일 다운로드 요청을 받아 해당 파일을 제공하는 메서드
     *
     * @param id 파일 ID
     * @return 파일의 리소스를 포함하는 ResponseEntity 객체
     */
    @GetMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestParam("seq") long id) throws IOException {
        // 파일 경로 디코딩
        String filePath = fileUtil.getUploadPath();

        // 파일명 디코딩
        FileDTO fileDTO = fileService.getFileById(id);

        // 파일명 인코딩 변환
        String encodedFilename = fileUtil.getEncodedFilename(fileDTO.getSavedName(), fileDTO.getExt());
        String headerValues = String.format("attachment; filename=\"%s\"", encodedFilename);

        // 파일에 대한 리소스 참조 생성
        Path newFile = Paths.get(filePath).resolve(fileDTO.getSavedName());
        Resource urlResource = new UrlResource(newFile.toUri());

        // 컨텐츠 타입을 결정
        MediaType contentType = MediaType.parseMediaType(fileUtil.getContentType(newFile));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(contentType);
        headers.add("Content-Disposition", headerValues);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK);
        response.put("headers", headers);
        response.put("urlResource", urlResource);

//        return ResponseUtil.getResponseEntity(response);
//        return ResponseEntity.ok()
//                .contentType(contentType)
//                .header("Content-Disposition", headerValues)
//                .body(urlResource);
//        return ResponseEntity.status(HttpStatus.OK)
        return ResponseUtil.getResponseEntity(response);
    }

    /**
     * 파일을 삭제하는 메서드
     *
     * @param fileId 삭제할 파일의 ID
     * @return HTTP 상태 200
     */
    @DeleteMapping("/{seq}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable("seq") long fileId) {
        fileService.delete(fileId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.NO_CONTENT);

//        return ResponseEntity.noContent().build();
        return ResponseUtil.getResponseEntity(response);
    }

}
