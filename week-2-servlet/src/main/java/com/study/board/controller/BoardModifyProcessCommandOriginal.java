package com.study.board.controller;

import com.study.board.dao.BoardDAO;
import com.study.board.dao.FileDAO;
import com.study.board.util.HttpHeaders;
import com.study.board.vo.BoardVO;
import com.study.board.vo.FileVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.*;

/**
 * 게시글 수정 요청을 처리하는 커맨드 클래스입니다.
 * 이 클래스는 사용자가 입력한 게시글 수정 데이터를 받아 데이터베이스에 저장합니다.
 */
public class BoardModifyProcessCommandOriginal implements Command {

    /**
     * 게시글 수정 요청을 처리합니다.
     * 사용자가 입력한 게시글 수정 데이터를 데이터베이스에 저장하고,
     * 저장이 완료된 후, 수정된 게시글의 상세보기 페이지로 리다이렉트합니다.
     *
     * @param request  클라이언트의 요청 정보를 담고 있는 HttpServletRequest 객체
     * @param response 클라이언트에게 응답을 보내는 HttpServletResponse 객체
     * @throws ServletException 요청 처리 중 발생하는 예외
     * @throws IOException 요청 또는 응답 처리 중 입출력 예외가 발생할 경우
     */
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 게시글 수정
        request.setCharacterEncoding(HttpHeaders.CHARSET_UTF_8);

        long id = Long.parseLong(request.getParameter("seq"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String writer = request.getParameter("writer");

        BoardVO boardVO = BoardVO.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .build();

        BoardDAO boardDAO = new BoardDAO();
        boardDAO.updateBoard(boardVO);
        // --- 게시글 수정 끝

        // 파일 첨부 처리
        String existsFileListParam = request.getParameter("file_list_exists");
        boolean existsFileList = "true".equals(existsFileListParam);

        // 추가적으로 첨부해야할 파일이 존재하는 경우만 처리
//        if (existsFileList) {
        String directoryPath = getSavedFileDirectoryPath();
        File uploadDirectory = new File(directoryPath);
        ensureUploadDirectoryExists(uploadDirectory);

        List<FileVO> fileList = new ArrayList<>();
//        BoardVO boardVO = extractFormData(request, fileList, uploadDirectory);

//            if (!fileList.isEmpty()) {  //
        FileDAO fileDAO = new FileDAO();
        fileDAO.insertFileList(fileList, id);
//            }
//        }

//        BoardDAO boardDAO = new BoardDAO();
//        boardDAO.updateBoard(boardVO);

        // 현재 게시글로 이동
        response.sendRedirect("view.do?seq=" + id);
    }

    /**
     * 설정 파일에서 업로드 디렉토리 경로를 로드
     *
     * @return 업로드 디렉토리 경로
     */
    private String getSavedFileDirectoryPath() {
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
     * @param uploadDir 파일을 업로드할 디렉토리
     */
    private void ensureUploadDirectoryExists(File uploadDir) {
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }

    /**
     * HttpServletRequest에서 폼 데이터를 추출하여 BoardVO 객체를 생성
     *
     * @param request 사용자 요청 정보를 담고 있는 HttpServletRequest 객체
     * @param fileList 업로드된 파일 정보를 저장할 리스트
     * @param uploadDir 파일을 업로드할 디렉토리
     * @return 생성된 BoardVO 객체
     * @throws IOException 입출력 처리 중 발생하는 예외
     * @throws ServletException 요청 처리 중 발생하는 예외
     */
    private BoardVO extractFormData(HttpServletRequest request, List<FileVO> fileList, File uploadDir)
            throws IOException, ServletException {

        BoardVO boardVO = null;

        // 요청의 파트(파일 포함)를 반복 처리
        Collection<Part> parts = request.getParts();
        for (Part part : parts) {
            if (part.getName().equals("upload_file") && part.getSize() > 0) {   // 파일인 경우
                handleFileUpload(part, fileList, uploadDir);
            } else {    // 파일이 아닌 경우
                // 파트 이름으로 폼 데이터 추출
                String fieldName = part.getName();
                String fieldValue = request.getParameter(part.getName());
                boardVO = handleFormData(fieldName, fieldValue);
            }
        }

        return boardVO;
    }

    private void handleFileUpload(Part part, List<FileVO> fileList, File uploadDir) throws IOException {
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

    private BoardVO handleFormData(String fieldName, String fieldValue) throws IOException {
        // 폼 필드 데이터 초기화
        int categoryId = 0;
        String title = "";
        String content = "";
        String writer = "";
        String password = "";

        // 파트 이름으로 폼 데이터 추출
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

        // Board 객체 반환
        return BoardVO.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .build();
    }
}
