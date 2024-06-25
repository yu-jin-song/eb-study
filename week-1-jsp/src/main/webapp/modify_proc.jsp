<%--
  Created by IntelliJ IDEA.
  User: jined
  Date: 2024-01-21
  Time: 오전 5:22
  DB UPDATE 처리 구현
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.study.connection.board.dao.BoardDAO"%>
<%@page import="com.study.connection.board.vo.BoardVO"%>

<%
    // 1. 게시글 수정 정보 추출
    request.setCharacterEncoding("UTF-8");
    long id = Long.parseLong(request.getParameter("seq"));
    String title = request.getParameter("title");
    String content = request.getParameter("content");
    String writer = request.getParameter("writer");

    // 2. DB UPDATE
    BoardVO vo = new BoardVO();
    BoardDAO boardDAO = new BoardDAO();

    vo.setId(id);
    vo.setTitle(title);
    vo.setContent(content);
    vo.setWriter(writer);

    boardDAO.updateBoard(vo);

    // 3. 현재 게시글로 이동
    response.sendRedirect("view.jsp?seq=" + id);
%>