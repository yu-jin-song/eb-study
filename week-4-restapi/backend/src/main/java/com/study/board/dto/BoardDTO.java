package com.study.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 게시판의 게시글 정보를 나타내는 DTO 클래스
 *
 * @author 송유진
 * TODO: 유효성 검사 구현 완료하기
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {

    private long boardId;

//    @NotBlank(message = "카테고리 선택은 필수입니다.")
    @NotBlank(message = "{common.error.required;카테고리,선택}")
    private int categoryId;

//    @NotBlank(message = "제목은 필수입니다.")
    @NotBlank(message = "{common.error.required;제목,입력}")
    @Size(
            min = 4, max = 99,
            message = "{validation.error.title}"
    )
    private String title;

//    @NotBlank(message = "내용은 필수입니다.")
    @NotBlank(message = "{common.error.required;내용,입력}")
    @Size(
            min = 4, max = 1999,
            message = "{validation.error.content}"
    )
    private String content;

    @NotBlank(message = "{common.error.required;작성자,입력}")
    @Size(
            min = 3, max = 4,
            message = "{validation.error.writer}"
    )
    private String writer;

    /**
     * 게시글 비밀번호
     * - 게시글 작성, 수정, 삭제 시에만 필요
     */
    @NotBlank(message = "{common.error.required;비밀번호,입력}")
    @Pattern(
            regexp = "^(?!.*\\s)(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).{4,15}$",
            message = "{validation.error.password}"
    )
    private String password;

    private long views;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    /**
     * 게시글이 속한 카테고리의 이름
     */
    private String categoryName;

    /**
     * 게시글에 첨부 파일이 있는지 여부를 나타내는 플래그
     * - 게시판 목록 조회에서만 사용
     */
    private boolean hasFiles;


    @Override
    public String toString() {
        return "BoardDTO{" +
                "boardId=" + boardId +
                ", categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer='" + writer + '\'' +
                ", password='" + password + '\'' +
                ", views=" + views +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", categoryName='" + categoryName + '\'' +
                ", hasFiles=" + hasFiles +
                '}';
    }
}
