package com.study.board.util;

import com.mysql.cj.util.StringUtils;
import com.study.board.controller.BoardWriteProcessCommand;
import com.study.board.vo.BoardVO;
import com.study.board.vo.FileVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;

public class FileUtil {
    /**
     * 설정 파일에서 업로드 디렉토리 경로를 로드
     *
     * @return 업로드 디렉토리 경로
     */
    public static String getSavedFileDirectoryPath() {
        Properties properties = new Properties();
        try (InputStream input =
                     BoardWriteProcessCommand.class.getClassLoader()
                             .getResourceAsStream("config.properties")
        ) {
            properties.load(input);
            return properties.getProperty("saved.directory");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null; // 또는 기본값을 반환
        }
    }

    /**
     * 업로드 디렉토리가 존재하는지 확인하고, 존재하지 않으면 생성
     *
     * @return 파일 업로드 디렉토리
     */
    public static File getUploadDirectory() {

        String directoryPath = getSavedFileDirectoryPath();

        // 파일을 upload 할 directory 생성
        File uploadDirectory = new File(directoryPath);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        return uploadDirectory;
    }

    private BoardVO extractFormData(HttpServletRequest request, List<FileVO> fileList, File uploadDir)
            throws IOException, ServletException {
        // 폼 필드 데이터 초기화
        int categoryId = 0;
        String title = "";
        String content = "";
        String writer = "";
        String password = "";

        // 요청의 파트(파일 포함)를 반복 처리
        Collection<Part> parts = request.getParts();
        for (Part part : parts) {
            if (part.getName().equals("upload_file") && part.getSize() > 0) {   // 파일인 경우
                uploadFile(part, fileList, uploadDir);
            } else {    // 파일이 아닌 경우
                // 파트 이름으로 폼 데이터 추출
                String fieldName = part.getName();
                String fieldValue = request.getParameter(part.getName());
                switch (fieldName) {
                    case "category":
                        categoryId = Integer.parseInt(fieldValue);
                        break;
                    case "title":
                        title = fieldValue;
                        break;
                    case "content":
                        content = fieldValue;
                        break;
                    case "writer":
                        writer = fieldValue;
                        break;
                    case "password":
                        password = fieldValue;
                        break;
                }
            }
        }

        // BoardVO 객체 생성 및 반환
        return BoardVO.builder()
                .categoryId(categoryId)
                .title(title)
                .content(content)
                .writer(writer)
                .password(password)
                .build();
    }

    private void uploadFile(Part part, List<FileVO> fileList, File uploadDir) throws IOException {
        // 파일 원본 이름, 확장자, 저장 이름 추출
        String originalName = part.getSubmittedFileName();
        String extension = FilenameUtils.getExtension(originalName);
        String savedName = UUID.randomUUID() + "_" + originalName;
        File file = new File(uploadDir, savedName);

        // 파일 저장
        try (InputStream inputStream = part.getInputStream();
             OutputStream outputStream = Files.newOutputStream(file.toPath())) {
            inputStream.transferTo(outputStream);
        }

        // 파일 정보 저장
        fileList.add(FileVO.builder()
                .originalName(originalName)
                .savedName(savedName)
                .savedPath(file.getAbsolutePath())
                .ext(extension)
                .size(part.getSize())
                .build());
    }
}
