<%--
  Created by IntelliJ IDEA.
  User: jined
  Date: 2024-01-27
  Time: 오전 12:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.board.vo.CategoryVO" %>

<%
    String id = request.getParameter("seq");

    List<CategoryVO> categoryList = (List<CategoryVO>) request.getAttribute("category_list");
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
        <form action="writeProcess.do" method="post" enctype="multipart/form-data">
            <input type="hidden" name="seq" value="<%= id %>" />
            <table border="1" cellpadding="0" cellspacing="0" align="center">
                <tr>
                    <td bgcolor="orange" width="70">카테고리*</td>
                    <td align="left">
                        <select name="category">
                            <option value="">카테고리 선택</option>
                        <% for (CategoryVO category : categoryList) { %>
                            <option value="<%= category.getId() %>"><%= category.getName() %></option>
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
                        <input type="password" id="password" name="password" placeholder="비밀번호" maxlength="16"/>
                    </td>
                    <td align="left">
                        <input type="password" id="verify_password" name="verify_password" placeholder="비밀번호 확인" maxlength="16"/>
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
            <% for (int i = 0; i < 3; i++) { %>
                <tr>
                    <td align="left">
                        <input type="file" name="upload_file"/>
                    </td>
                </tr>
            <% } %>
                <tr>
                    <td colspan="2" align="center">
                        <button type="button" onclick="location.href='list.do'">취소</button>
                        <input type="submit" value="저장"/>
                    </td>
                </tr>
            </table>
        </form>
        <%--게시글 작성 폼 끝--%>

        <hr>

    <script type="text/javascript">
        // var password = document.getElementById('password');
        // var verify_password = document.getElementById('verify_password');
        //
        // if (password != verify_password) {
        //     console.log("비밀번호가 맞지 않습니다.")
        //     // 데이터 처리 등 아무것도 처리 x
        // }
    </script>
    </body>
</html>
