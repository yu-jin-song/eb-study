<%--
  Created by IntelliJ IDEA.
  User: jined
  Date: 2024-01-20
  Time: 오후 9:03
  게시글 작성 관련 DB 작업 구현
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.study.connection.board.dao.BoardDAO"%>
<%@page import="com.study.connection.board.vo.BoardVO"%>

<%
  // 1. 게시글 입력 정보 추출
  request.setCharacterEncoding("UTF-8");
  int category_id = Integer.parseInt(request.getParameter("category"));
  String title = request.getParameter("title");
  String content = request.getParameter("content");
  String writer = request.getParameter("writer");
  String password = request.getParameter("password");

  // 2. DB INSERT 처리
  BoardVO vo = new BoardVO();
  vo.setCategoryId(category_id);
  vo.setTitle(title);
  vo.setContent(content);
  vo.setWriter(writer);
  vo.setPassword(password);

  BoardDAO boardDAO = new BoardDAO();
  long id = boardDAO.insertBoard(vo);

  // 3. 작성한 게시글로 이동
  response.sendRedirect("view.jsp?seq=" + id);
%>