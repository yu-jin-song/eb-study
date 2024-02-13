package com.study.board.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 파일 정보를 담는 Value Object 클래스입니다.
 * 파일의 ID, 연관된 게시글 ID, 원본 파일명, 저장된 파일명, 저장 경로, 확장자, 파일 크기 등의 정보를 관리합니다.
 */
@Setter
@Getter
@Builder
public class FileVO {
    private long id;
    private long boardId;
    private String originalName;
    private String savedName;
    private String savedPath;
    private String ext;
    private long size;
}
