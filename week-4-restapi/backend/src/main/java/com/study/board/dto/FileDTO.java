package com.study.board.dto;

import lombok.*;

/**
 * 파일 정보를 나타내는 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDTO {

    private long fileId;
    private long boardId;
    private String originalName;
    private String savedName;
    private String savedPath;
    private String ext;
    private long size;

    @Override
    public String toString() {
        return "File{" +
                "fileId=" + fileId +
                ", boardId=" + boardId +
                ", originalName='" + originalName + '\'' +
                ", savedName='" + savedName + '\'' +
                ", savedPath='" + savedPath + '\'' +
                ", ext='" + ext + '\'' +
                ", size=" + size +
                '}';
    }
}
