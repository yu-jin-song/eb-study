package com.study.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 게시판의 게시글 정보를 나타내는 DTO 클래스
 * TODO : 유효성 처리 진행중
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {
    private long boardId;

    @NotNull(message = "카테고리를 선택해주세요.")
    private int categoryId;

    private String categoryName;

    @NotBlank(message = "제목은 필수입니다.")
    @Size(min = 4, max = 99, message = "제목은 4자 이상 100자 미만입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    @Size(min = 4, max = 1999, message = "내용은 4자 이상 2000자 미만입니다.")
    private String content;

    private boolean hasFiles;

    @NotBlank(message = "작성자는 필수입니다.")
    @Size(min = 3, max = 4, message = "작성자는 3자 이상 5자 미만입니다.")
    private String writer;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{4,15}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    private long views;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;


    public String toString() {
        return "BoardDTO{" +
                "boardId=" + boardId +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", hasFiles=" + hasFiles +
                ", writer='" + writer + '\'' +
                ", password='" + password + '\'' +
                ", views=" + views +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}