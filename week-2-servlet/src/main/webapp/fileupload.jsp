<%--
  Created by IntelliJ IDEA.
  User: jined
  Date: 2024-01-31
  Time: 오후 6:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>파일첨부 테스트</title>
    </head>
    <body>
    <form action="file.do" method="post" enctype="multipart/form-data">
        <table border="1" cellpadding="0" cellspacing="0" align="center">
        <% for (int i=0; i<3; i++) { %>
            <tr>
                <td bgcolor="orange">파일 첨부</td>
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
    </body>
</html>
