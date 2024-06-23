package com.study.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 비밀번호 확인 요청 파라미터 관리 DTO 클래스
 */
@Getter
@Setter
public class PasswordCheckRequest {

    @NotBlank(message = "{common.error.required;게시판,ID}")
    private String boardId;

    @NotBlank(message = "{common.error.required;비밀번호,입력}")
    @Pattern(
            regexp = "^(?!.*\\s)(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).{4,15}$",
            message = "{validation.error.password}"
    )
    private String password;

    public long getBoardId() {
        return Long.parseLong(boardId);
    }

    @Override
    public String toString() {
        return "PasswordCheckRequest{" +
                "boardId='" + boardId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
