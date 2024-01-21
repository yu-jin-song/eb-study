<%--
  Created by IntelliJ IDEA.
  User: jined
  Date: 2024-01-20
  Time: 오후 6:44
  게시글 상세 조회 페이지 구현
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="com.study.connection.board.*" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.connection.board.vo.BoardVO" %>
<%@ page import="com.study.connection.board.vo.CommentVO" %>
<%@ page import="com.study.connection.board.dao.BoardDAO" %>
<%@ page import="com.study.connection.board.dao.CategoryDAO" %>
<%@ page import="com.study.connection.board.dao.CommentDAO" %>

<%
    // 상세 조회할 게시글 번호 추출
    String seq = request.getParameter("seq");

    // 상세 조회할 게시글 정보 가져오기
    BoardVO vo = new BoardVO();
    vo.setId(Long.parseLong(seq));

    BoardDAO boardDAO = new BoardDAO();
    BoardVO board = boardDAO.getBoard(vo);
    long views = boardDAO.updateViewsCnt(board);    // 조회수 갱신

    CategoryDAO categoryDAO = new CategoryDAO();
    String categoryName = categoryDAO.getCategoryById(board.getCategoryId()).getName();   // 현재 글의 카테고리명 조회

    boolean existFile = false;

    // 댓글 목록 가져오기
    CommentVO commentVO = new CommentVO();
    CommentDAO commentDAO = new CommentDAO();
    List<CommentVO> commentList = commentDAO.getCommentList();
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
            <td align="left"><%=board.getWriter() %></td>
            <td align="left">등록일시 <%=board.getCreatedAt() %></td>
            <td align="left">수정일시 <%=board.getModifedAt() %></td>
        </tr>
            <td align="left">[<%=categoryName %>]</td>
            <td align="left"><%=board.getTitle() %></td>
            <td align="left">조회수: <%=views %></td>
        </tr>
<%--        <tr>--%>
<%--            <td align="left"><%=board.getContent() %></td>--%>
<%--        </tr>--%>
<%--        <tr>--%>
<%--            <% if(existFile) { %>--%>
<%--&lt;%&ndash;            첨부파일 출력 로직 작성&ndash;%&gt;--%>
<%--            <% } %>--%>
<%--        </tr>--%>
    </table>
<%=board.getContent() %>

<br><br><br><br><br>

<%--댓글 처리(미완성) : 댓글 INSERT 시 에러 발생--%>
<form action="comment_proc.jsp" method="post">
    <input type="hidden" name="board_id" value="<%=board.getId()%>"/>
    <input type="hidden" name="created_at" value="<%=commentVO.getCreatedAt()%>"/>

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
                <input type="submit" value="등록"/>
            </td>
        </tr>
    </table>
</form>
<%--댓글 처리 끝--%>

<hr>

<a href="list.jsp">목록</a>
<a href="modify.jsp?seq=<%=board.getId() %>">수정</a>&nbsp;&nbsp;&nbsp;
<a href="delete.jsp?seq=<%=board.getId() %>">삭제</a>&nbsp;&nbsp;&nbsp;


</body>
</html>
