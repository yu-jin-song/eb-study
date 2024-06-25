package com.study.board.service;

import com.study.board.dao.FileMapper;
import com.study.board.dto.FileDTO;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 파일 관련 서비스를 제공하는 클래스
 */
@Service
public class FileService {

    private final FileMapper fileMapper;

    /**
     * FileService 생성자
     *
     * @param fileMapper 파일 데이터 접근을 위한 MyBatis 매퍼
     */
    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

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
     * 파일을 다운로드 하는 메서드
     *
     * @param fileName  파일명
     * @param filePath  파일 경로
     * @param extension 확장자
     */
    public void downloadFile(String fileName, String filePath, String extension) {
        // TODO : 추후 구현
        // 파일 이름과 경로 디코딩
//        String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
//        String decodedFilePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
//
//        // 파일에 대한 리소스 참조 생성
//        Path file = Paths.get(decodedFilePath).resolve(decodedFileName);
//        Resource resource = new UrlResource(file.toUri());
//
//        // 컨텐츠 타입을 결정
//        String contentType = Files.probeContentType(file);
//        if (Objects.isNull(contentType)) {
//            // 파일 타입을 결정할 수 없는 경우 기본값 사용
//            contentType = "application/octet-stream";
//        }
//
//        // 파일명 인코딩 변환
//        String encodedFileName = new String(
//                decodedFileName.getBytes(StandardCharsets.UTF_8),
//                StandardCharsets.ISO_8859_1);
//        encodedFileName += "." + extension;
    }

    /**
     * 파일 업로드 처리 메서드
     * - 새로운 파일 저장, 기존 파일 유지, 더 이상 사용되지 않는 파일 삭제
     *
     * @param boardId      게시글의 ID
     * @param newFiles     업로드할 파일들의 리스트
     * @param existedFiles 기존 파일 리스트
     * @throws IOException 파일 저장 실패 시 발생
     */
    public void handleFileUpload(Long boardId, List<MultipartFile> newFiles, List<String> existedFiles) throws IOException {
        // 새로 업로드된 파일 처리
        if (!Objects.isNull(newFiles) && !newFiles.isEmpty()) {
            List<FileDTO> uploadFiles = new ArrayList<>();
            for (MultipartFile file : newFiles) {
                if (!file.isEmpty()) {
                    // 서버에 새로운 파일 저장
                    uploadFiles.add(saveFileAndGetFileDTO(boardId, file));
                }
            }
            // DB에 파일 메타 데이터 저장
            writeFilesToDB(boardId, uploadFiles);
        }

        // 사용자가 삭제한 파일 DB에서 삭제
        if (!Objects.isNull(existedFiles) && !existedFiles.isEmpty()) {
            // 기존 파일 목록 조회(DB)
            List<FileDTO> existedDBFiles = fileMapper.getAllFilesByBoardId(boardId);

            // view에 존재하는 기존 파일 이름 목록
            Set<String> existedFileNames = new HashSet<>(existedFiles);
            existedDBFiles.forEach(file -> {
                if (!existedFileNames.contains(file.getOriginalName())) {
                    fileMapper.deleteFileById(file.getFileId());
                }
            });
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
        String originalFilename = FilenameUtils.getBaseName(file.getOriginalFilename());
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String savedFilename = System.currentTimeMillis() + "_" + originalFilename;
        // TODO : configuration에서 얻어오는 걸로 변경
        String directoryPath = "D:" + File.separator + File.separator + "eBrainSoft" + File.separator + "upload";

        File uploadDirectory = new File(directoryPath);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        File savedFile = new File(uploadDirectory, savedFilename);

        file.transferTo(savedFile);

        return FileDTO.builder()
                .boardId(boardId)
                .originalName(originalFilename)
                .savedName(savedFilename)
                .savedPath(uploadDirectory.getAbsolutePath())
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
    public void writeFilesToDB(long boardId, List<FileDTO> files) {
        if (!files.isEmpty()) {
            fileMapper.insertFiles(files, boardId);
        }
    }

}
