<%--
  Created by IntelliJ IDEA.
  User: jined
  Date: 2024-01-27
  Time: 오전 1:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.study.board.vo.BoardVO" %>
<%@ page import="com.study.board.vo.FileVO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Objects" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="com.study.board.util.HttpHeaders" %>

<%
    BoardVO board = (BoardVO) request.getAttribute("board");
    List<FileVO> fileList = (List<FileVO>) request.getAttribute("file_list");
    String categoryName = (String) request.getAttribute("category_name");
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
        <%-- 수정할 정보 modify_proc.jsp에서 DB 처리 --%>
        <form action="modifyProcess.do" method="post" enctype="multipart/form-data">
            <%-- DB에 저장된 데이터의 정보를 수정하기 위한 PK값--%>
            <input type="hidden" name="seq" value="<%= board.getId() %>" />
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
                        <input type="text" name="writer" value="<%= board.getWriter() %>"/>
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
                        <input type="text" name="title" value="<%= board.getTitle() %>"/>
                    </td>
                </tr>
                <tr>
                    <td bgcolor="orange">내용</td>
                    <td align="left">
                        <textarea rows="10" cols="40" name="content"><%= board.getContent() %></textarea>
                    </td>
                </tr>
            <%-- 파일 첨부 --%>
            <%
                int fileListSize = fileList.size(); // 파일 목록 크기 확인

                for (int i = 0; i < 3; i++) {   // 첨부란
                    if (i < fileListSize) { // 파일 정보가 존재하는 경우
                        FileVO fileVO = fileList.get(i);
                        String fileName = URLEncoder.encode(fileVO.getSavedName(), HttpHeaders.CHARSET_UTF_8);
                        String filePath = URLEncoder.encode(fileVO.getSavedPath(), HttpHeaders.CHARSET_UTF_8);
            %>
                <input type="hidden" id="file_id_<%= i %>" name="file_id_<%= i %>" value="<%= fileVO.getId() %>" />
                <input type="hidden" id="file_name_<%= i %>" name="file_name_<%= i %>" value="<%= fileVO.getOriginalName() %>" />
                <tr id="file_<%= i %>">
                    <td align="left">
                        <%= fileVO.getOriginalName() %>
                        <button type="button" onclick="location.href='/file.do?file_name=<%= fileName %>&file_path=<%= filePath %>'">Download</button>
                        <%-- TODO : 삭제 기능 구현 필요 --%>
                         <button type="button" onclick="deleteFile('<%= i %>')">Delete</button>
                    </td>
                </tr>
            <%      } else {    // 파일 정보가 존재하지 않는 경우 %>
                <tr>
                    <td align="left">
                        <input type="file" name="upload_file"/>
                    </td>
                </tr>
            <%
                    }
                }
            %>
            <%-- 파일 첨부 끝 --%>
                <tr>
                    <td colspan="2" align="center">
                        <%-- 취소 버튼 클릭 시 해당 글 상세보기로 이동 --%>
                        <button type="button" onclick="location.href='view.do?seq=<%= board.getId() %>'">취소</button>
                        <input type="submit" value="저장"/>
                    </td>
                </tr>
            </table>
        </form>

        <hr>

        <script type="text/javascript">
            function deleteFile(fileId) {
                // file id hidden input 삭제
                const fileIdInput = document.getElementById("file_id_" + fileId);
                if (fileIdInput) {
                    fileIdInput.parentNode.removeChild(fileIdInput);
                }

                // file name hidden input 삭제
                const fileNameInput = document.getElementById("file_name_" + fileId);
                if (fileNameInput) {
                    fileNameInput.parentNode.removeChild(fileNameInput);
                }

                // 파일 삭제
                const file = document.getElementById("file_" + fileId);
                if (file) {
                    file.innerHTML = '<td align="left"><input type="file" name="upload_file" /></td>';
                }
            }
        </script>

    </body>
</html>