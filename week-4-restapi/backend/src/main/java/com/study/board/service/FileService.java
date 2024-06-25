package com.study.board.service;

import com.study.board.dao.FileMapper;
import com.study.board.dto.FileDTO;
import com.study.board.util.FileUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 파일 관련 서비스를 제공하는 클래스
 */
@AllArgsConstructor
@Slf4j
@Service
public class FileService {

    private final FileMapper fileMapper;
    private final FileUtil fileUtil;


    /**
     * 특정 게시글 ID에 해당하는 모든 파일의 리스트를 조회하는 메서드
     *
     * @param boardId 게시글의 ID
     * @return 해당 게시글에 업로드된 파일들의 리스트
     */
    public List<FileDTO> getAllFilesByBoardId(long boardId) {
        return  fileMapper.getAllFilesByBoardId(boardId);
    }

    /**
     * 파일 다운로드를 위한 정보 조회 및 준비 메서드
     *
     * @param id 파일 ID
     * @return FileDownloadResponse 객체
     */
    public FileDTO getFileById(long id) {
        return fileMapper.getFileById(id);
    }

    /**
     * 특정 ID를 가진 파일 삭제 메서드
     *
     * @param fileId 삭제할 파일의 ID
     */
    public void delete(long fileId) {
        fileMapper.delete(fileId);
    }

    /**
     * 파일 업로드 처리 메서드
     * - 새로운 파일 저장, 기존 파일 유지
     *
     * @param boardId      게시글의 ID
     * @param newFiles     업로드할 파일들의 리스트
     */
    public void handleFileUpload(Long boardId, List<MultipartFile> newFiles) {
        if (!Objects.isNull(newFiles) && !newFiles.isEmpty()) {
            List<FileDTO> uploadFiles = newFiles.stream()
                    .filter(file -> !file.isEmpty())    // 빈 파일 제거
                    .map(file -> {  // 서버에 새로운 파일 저장 및 FileDTO 반환
                        try {
                            return saveFileAndGetFileDTO(boardId, file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());  // 새 파일 목록 생성

            // DB에 파일 메타 데이터 저장
            writeFilesToDB(boardId, uploadFiles);
        }
    }

    /**
     * 파일을 저장하고, 해당 파일의 메타데이터를 반환
     *
     * @param boardId 게시글의 ID
     * @param file 저장할 파일
     * @throws IOException 파일 저장 실패 시 발생
     */
    private FileDTO saveFileAndGetFileDTO(Long boardId, MultipartFile file) throws IOException {
        // 파일 저장 경로
        String uploadPath = fileUtil.getUploadPath();

        // 저장 디렉토리
        File uploadDirectory = fileUtil.getUploadDirectory(uploadPath);

        // 원본 파일명 및 저장 파일명
        String originalFilename = FilenameUtils.getBaseName(file.getOriginalFilename());
        String savedFilename = fileUtil.getSavedFilename(originalFilename);

        // 파일 저장
        File savedFile = new File(uploadDirectory, savedFilename);
        file.transferTo(savedFile);

        // 확장자 추출
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        return FileDTO.builder()
                .boardId(boardId)
                .originalName(originalFilename)
                .savedName(savedFilename)
                .savedPath(fileUtil.getDirectoryName())
                .ext(fileExtension)
                .size(file.getSize())
                .build();
    }

    /**
     * 파일 리스트를 데이터베이스에 저장하는 메서드
     *
     * @param files 저장할 파일 리스트
     * @param boardId 파일이 속한 게시글의 ID
     */
    private void writeFilesToDB(long boardId, List<FileDTO> files) {
        if (!files.isEmpty()) {
            fileMapper.insertFiles(files, boardId);
        }
    }

}
