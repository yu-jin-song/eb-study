<%@ page import="com.study.connection.board.vo.CategoryVO" %><%--
  Created by IntelliJ IDEA.
  User: jined
  Date: 2024-01-20
  Time: 오후 6:43
  게시글 작성 구현
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>
<%@page import="com.study.connection.board.dao.CategoryDAO"%>
<%@page import="com.study.connection.board.vo.CategoryVO"%>

<%
    // 카테고리 목록 가져오기
    CategoryVO vo = new CategoryVO();
    CategoryDAO categoryDAO = new CategoryDAO();
    List<CategoryVO> categoryList = categoryDAO.getCategoryList();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시판 등록</title>
</head>
<body align="center">

<h1>게시판 - 등록</h1>

<hr>

<%--게시글 작성 폼--%>
<form action="write_proc.jsp" method="post">
    <table border="1" cellpadding="0" cellspacing="0" align="center">
        <tr>
            <td bgcolor="orange" width="70">카테고리*</td>
            <td align="left">
                <select name="category">
                    <option value="">카테고리 선택</option>
                <% for (CategoryVO category : categoryList) { %>
                    <option value="<%=category.getId()%>"><%=category.getName()%></option>
                <% } %>
                </select>
            </td>
        </tr>
        <tr>
            <td bgcolor="orange" width="70">작성자*</td>
            <td align="left">
                <input type="text" name="writer"/>
            </td>
        </tr>
        <tr>
            <td bgcolor="orange" width="70">비밀번호*</td>
            <td align="left">
                <input type="password" name="password" placeholder="비밀번호" maxlength="16"/>
            </td>
            <td align="left">
                <input type="password" name="verify_password" placeholder="비밀번호 확인" maxlength="16"/>
            </td>
        </tr>
        <tr>
            <td bgcolor="orange" width="70">제목*</td>
            <td align="left">
                <input type="text" name="title"/>
            </td>
        </tr>
        <tr>
            <td bgcolor="orange">내용*</td>
            <td align="left">
                <textarea rows="10" cols="40" name="content"></textarea>
            </td>
        </tr>
        <tr>
<%--            파일 첨부 : 아직 미구현--%>
            <td bgcolor="orange">파일 첨부</td>
            <td align="left">
                <input type="file" name="upload_file"/>
                <input type="file" name="upload_file"/>
                <input type="file" name="upload_file"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                <button type="button" onclick="location.href='list.jsp'">취소</button>
                <input type="submit" value="저장"/>
            </td>
        </tr>
    </table>
</form>
<%--게시글 작성 폼 끝--%>


<hr>


</body>
</html>