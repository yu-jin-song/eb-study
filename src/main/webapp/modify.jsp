<%--
  Created by IntelliJ IDEA.
  User: jined
  Date: 2024-01-21
  Time: 오전 5:00
  게시글 수정 구현
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.study.connection.board.dao.BoardDAO"%>
<%@page import="com.study.connection.board.vo.BoardVO"%>
<%@page import="com.study.connection.board.dao.CategoryDAO"%>

<%
    // 1. 수정할 게시글 번호 추출
    String id = request.getParameter("seq");

    // 2. 수정할 게시글 정보 가져오기
    BoardVO vo = new BoardVO();
    vo.setId(Long.parseLong(id));

    BoardDAO boardDAO = new BoardDAO();
    BoardVO board = boardDAO.getBoard(vo);

    // 3. 카테고리명 가져오기
    CategoryDAO categoryDAO = new CategoryDAO();
    String categoryName = categoryDAO.getCategoryById(board.getCategoryId()).getName();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>글 상세</title>
</head>
<body align="center">

<h1>게시판 - 수정</h1>
<hr>


기본 정보
<%--수정할 정보 modify_proc.jsp에서 DB 처리--%>
<form action="modify_proc.jsp" method="post">
<%--    DB에 저장된 데이터의 정보를 수정하기 위한 PK값--%>
    <input type="hidden" name="seq" value="<%=board.getId() %>"/>
    <table border="1" cellpadding="0" cellspacing="0" align="center">
        <tr>
            <td bgcolor="orange" width="70">카테고리*</td>
            <td align="left"><%= categoryName %></td>
        </tr>
        <tr>
            <td bgcolor="orange" width="70">등록 일시</td>
            <td align="left"><%= board.getCreatedAt() %></td>
        </tr>
        <tr>
            <td bgcolor="orange" width="70">수정 일시</td>
            <td align="left"><%= board.getModifedAt() %></td>
        </tr>
        <tr>
            <td bgcolor="orange" width="70">조회수</td>
            <td align="left"><%= board.getViews() %></td>
        </tr>
        <tr>
            <td bgcolor="orange">작성자*</td>
            <td align="left">
                <input type="text" name="writer" value="<%=board.getWriter() %>"/>
            </td>
        </tr>
        <tr>
            <td bgcolor="orange" width="70">비밀번호</td>
            <td align="left">
                <input type="password" name="password" placeholder="비밀번호" maxlength="16"/>
            </td>
        </tr>
        <tr>
            <td bgcolor="orange" width="70">제목</td>
            <td align="left">
                <input type="text" name="title" value="<%=board.getTitle() %>"/>
            </td>
        </tr>
        <tr>
            <td bgcolor="orange">내용</td>
            <td align="left">
                <textarea rows="10" cols="40" name="content"><%=board.getContent() %></textarea>
            </td>
        </tr>
<%--        파일 첨부(아직 미구현)--%>
        <tr>
            <td bgcolor="orange">파일 첨부</td>
            <td align="left">
                <input type="file" name="upload_file"/>
                <input type="file" name="upload_file"/>
                <input type="file" name="upload_file"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
<%--                취소 버튼 클릭 시 해당 글 상세보기로 이동--%>
                <button type="button" onclick="location.href='view.jsp?seq=<%=id%>'">취소</button>
                <input type="submit" value="저장"/>
            </td>
        </tr>
    </table>
</form>

<hr>


</body>
</html>