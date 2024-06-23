package com.study.board.util;

import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * 파일 처리 관련 유틸 클래스
 */
@Getter
@Builder
public class FileUtil {

    private String basePath;
    private String directoryName;

    /**
     * 파일이 실제로 저장되는 경로 가져오기
     *
     * @return 파일 실제 저장 경로
     */
    public String getUploadPath() {
        return basePath + File.separator + directoryName;
    }

    /**
     * 파일을 저장할 디렉토리 생성 및 반환
     *
     * @param uploadPath 파일을 저장할 기본 경로
     * @return 파일을 저장할 디렉토리
     */
    public File getUploadDirectory(String uploadPath) {
        File uploadDirectory = new File(uploadPath);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        return uploadDirectory;
    }

    /**
     * 서버에 저장될 파일이름 반환
     *
     * @param originalFilename 원본 파일명
     * @return 저장 파일명
     */
    public String getSavedFilename(String originalFilename) {
        return System.currentTimeMillis() + "_" + originalFilename;
    }

    /**
     * 디코딩 된 파일명 반환
     *
     * @param savedName 파일 저장명
     * @return 디코딩 된 파일명
     */
    public String getDecodedFilename(String savedName) {
        return URLDecoder.decode(savedName, StandardCharsets.UTF_8);
    }

    /**
     * 디코딩 된 파일 경로 반환
     *
     * @param uploadPath 파일 업로드 경로
     * @return 디코딩 된 파일 경로
     */
    public String getDecodedFilePath(String uploadPath) {
        return URLDecoder.decode(uploadPath, StandardCharsets.UTF_8);
    }

    /**
     * 파일명 인코딩 변환 및 반환
     *
     * @param decodedFilename 디코딩된 파일명
     * @param extension       확장자
     * @return 인코딩 된 파일명
     */
    public String getEncodedFilename(String decodedFilename, String extension) {
        String encodedFilename = URLEncoder
                .encode(decodedFilename, StandardCharsets.ISO_8859_1)
                .replace("+", "%20"); // 공백 문자 처리
        encodedFilename += "." + extension;

        return  encodedFilename;
    }

    /**
     * 파일 컨텐츠 타입 반환
     *
     * @param file 파일 경로
     * @return 파일 컨텐츠 타입
     * @throws IOException 파일의 컨텐츠 타입을 결정할 수 없는 경우 발생하는 IO 예외
     */
    public String getContentType(Path file) throws IOException {
        // 파일의 컨텐츠 타입 결정
        String contentType = Files.probeContentType(file);

        // 파일 타입을 결정할 수 없는 경우 기본값 사용
        contentType = (Objects.isNull(contentType) ?
                "application/octet-stream" : contentType);

        return contentType;
    }
}
