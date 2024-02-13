<%--
  Created by IntelliJ IDEA.
  User: jined
  Date: 2024-01-26
  Time: 오후 11:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.study.board.vo.BoardVO" %>
<%@ page import="com.study.board.vo.CommentVO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.study.board.vo.FileVO" %>
<%@ page import="com.study.board.util.HttpHeaders" %>

<%
    BoardVO board = (BoardVO) request.getAttribute("board");
    List<FileVO> fileList = (List<FileVO>) request.getAttribute("file_list");
    String categoryName = (String) request.getAttribute("category_name");
    List<CommentVO> commentList = (List<CommentVO>) request.getAttribute("comment_list");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>글 상세</title>
    </head>
    <body align="center">

        <h1>게시판 - 보기</h1>

        <hr>

        <table border="1" cellpadding="0" cellspacing="0" align="center">
            <tr>
                <tr>
                    <td align="left"><%= board.getWriter() %></td>
                    <td align="left">등록일시 <%= board.getCreatedAt() %></td>
                    <td align="left">수정일시 <%= board.getModifedAt() %></td>
                </tr>
                <td align="left">[<%= categoryName %>]&nbsp;<%= board.getTitle() %></td>
                <td align="left">조회수: <%= board.getViews() %></td>
            </tr>
            <tr>
                <td align="left"><%=board.getContent() %></td>
            </tr>
            <tr>
                <td>
            <%-- 첨부파일이 존재하는 경우 --%>
            <%
                if (!fileList.isEmpty()) {
                    for (FileVO fileVO : fileList) {
                        String fileSavedName = URLEncoder.encode(fileVO.getSavedName(), HttpHeaders.CHARSET_UTF_8);
                        String fileSavedPath = URLEncoder.encode(fileVO.getSavedPath(), HttpHeaders.CHARSET_UTF_8);
            %>
                <a href="/file.do?file_name=<%= fileSavedName %>&file_path=<%= fileSavedPath %>"><%= fileVO.getOriginalName() %></a>
                <br>
            <%
                    }
                }
            %>
                </td>
            </tr>
        </table>
<%--        <%= board.getContent() %>--%>

        <br><br><br><br><br>

        <%-- 댓글 처리 --%>
        <form action="comment.do" method="post">
            <input type="hidden" name="board_id" value="<%=board.getId()%>"/>

            <table border="1" cellpadding="0" cellspacing="0" width="700" align="center">
                <% for (CommentVO comment : commentList) {%>
                <tr>
                    <td><%=comment.getWriter()%>&nbsp;&nbsp;&nbsp;&nbsp;<%=comment.getCreatedAt()%></td>
                    <td><%=comment.getComment()%></td>
                </tr>
                <% } %>
                <tr>
                    <td align="left">
                        작성자
                        <input type="text" name="writer"/>
                    </td>
                </tr>
                <tr>
                    <td align="left">
                        <textarea rows="3" cols="80" name="comment" placeholder="댓글을 입력해 주세요."></textarea>
                        <input type="submit" value="등록" />
                    </td>
                </tr>
            </table>
        </form>
        <%--댓글 처리 끝--%>

        <hr>

        <a href="list.do">목록</a>
        <a href="modifyForm.do?seq=<%= board.getId() %>">수정</a>&nbsp;&nbsp;&nbsp;
        <a href="delete.do?seq=<%= board.getId() %>">삭제</a>

    </body>
</html>
