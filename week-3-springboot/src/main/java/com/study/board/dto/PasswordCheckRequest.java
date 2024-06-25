package com.study.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordCheckRequest {
    private String boardId;
    private String password;

    public long getBoardId() {
        return Long.parseLong(boardId);
    }

    @Override
    public String toString() {
//        String.format( "PasswordCheckRequest{%s, %s}", boardId, password );
        return "PasswordCheckRequest{" +
                "boardId='" + boardId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
