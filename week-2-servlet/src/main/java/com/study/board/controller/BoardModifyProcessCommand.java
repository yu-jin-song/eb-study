package com.study.board.controller;

import com.google.gson.Gson;
import com.study.board.dao.BoardDAO;
import com.study.board.dao.FileDAO;
import com.study.board.util.FileUtil;
import com.study.board.util.HttpHeaders;
import com.study.board.vo.BoardVO;
import com.study.board.vo.FileVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class BoardModifyProcessCommand implements Command {

    Logger LOGGER = LoggerFactory.getLogger(BoardModifyProcessCommand.class);

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

        // 파일 첨부 처리
//        String directoryPath = getSavedFileDirectoryPath();
//        String directoryPath = FileUtil.getSavedFileDirectoryPath();
//        File uploadDirectory = new File(directoryPath);
//        ensureUploadDirectoryExists(uploadDirectory);
//        FileUtil.ensureUploadDirectoryExists(uploadDirectory);
        File uploadDirectory = FileUtil.getUploadDirectory();
        FileDAO fileDAO = new FileDAO();

        // 폼 데이터 및 파일 데이터 처리
        BoardVO boardVO = processFormDataAndFiles(request, fileDAO, uploadDirectory);
        BoardDAO boardDAO = new BoardDAO();
        boardDAO.updateBoard(boardVO);

        // 현재 게시글로 이동
        response.sendRedirect("view.do?seq=" + boardVO.getId());
    }

    private BoardVO processFormDataAndFiles(
            HttpServletRequest request,
            FileDAO fileDAO,
            File uploadDir
    ) throws IOException, ServletException {

        long boardId = 0L;
        String title = "";
        String content = "";
        String writer = "";

        Gson gson = new Gson();
        Collection<Part> parts = request.getParts();
        List<Part> fileList = new ArrayList<>();
        for (Part part : parts) {
            String partName = part.getName();
            if ((partName.equals("upload_file") || partName.startsWith("file_name")) && part.getSize() > 0) {
                fileList.add(part);
            } else {

                String fieldValue = request.getParameter(part.getName());

                if (partName.equals("seq")) {
                    boardId = Long.parseLong(fieldValue);
                } else if (partName.equals("title")) {
                    title = fieldValue;
                } else if (partName.equals("content")) {
                    content = fieldValue;
                } else if (partName.equals("writer")) {
                    writer = fieldValue;
                }
            }
        }

        LOGGER.debug("### boardId = " + boardId);
        processFiles(request, fileList, boardId, fileDAO, uploadDir);

        return BoardVO.builder()
                .id(boardId)
                .title(title)
                .content(content)
                .writer(writer)
                .build();
    }

    private void processFiles(
            HttpServletRequest request,
            List<Part> parts,
            long boardId,
            FileDAO fileDAO,
            File uploadDir
    ) throws IOException {
        List<FileVO> existingFileList = fileDAO.getFileList(boardId);

        Set<String> existingFileNameSet = new HashSet<>();
        for (FileVO file : existingFileList) {
            existingFileNameSet.add(file.getOriginalName());
        }

        Set<String> uploadedFileNameSet = new HashSet<>();
        for (Part part : parts) {
            String fileName = request.getParameter(part.getName());
            if (!existingFileNameSet.contains(fileName)) {
                // 새로운 파일이므로 DB에 삽입
                FileVO newFile = uploadFile(part, uploadDir);
                fileDAO.insertFile(newFile, boardId);
            }
            // 업로드된 파일 목록에 추가
            uploadedFileNameSet.add(fileName);
        }

        // DB에는 있지만, 업로드된 파일 목록에 없는 파일을 찾아 삭제 처리
        existingFileList.stream()
                .filter(file -> !uploadedFileNameSet.contains(file.getOriginalName()))
                .forEach(file -> fileDAO.deleteFile(file.getId()));
    }


    private FileVO uploadFile(Part part, File uploadDir) throws IOException {
        String originalName = part.getSubmittedFileName();
        String extension = FilenameUtils.getExtension(originalName);
        String savedName = UUID.randomUUID() + "_" + originalName;
        File file = new File(uploadDir, savedName);

        try (InputStream input = part.getInputStream();
             OutputStream output = Files.newOutputStream(file.toPath())) {
            input.transferTo(output);
        }

        // 여기서 FileVO 객체 생성 및 반환
        return FileVO.builder()
                .originalName(originalName)
                .savedName(savedName)
                .savedPath(uploadDir.getAbsolutePath())
                .ext(extension)
                .size(part.getSize())
                .build();
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
}
