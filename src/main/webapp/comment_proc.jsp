<%--
  Created by IntelliJ IDEA.
  User: jined
  Date: 2024-01-21
  Time: 오전 10:19
  댓글 DB INSERT 구현
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.study.connection.board.dao.CommentDAO"%>
<%@page import="com.study.connection.board.vo.CommentVO"%>

<%
    // 1. 댓글 입력 정보 추출 : board_id 저장할 때 에러 발생
    request.setCharacterEncoding("UTF-8");
//    System.out.println("넘어오는 값 확인");
//    System.out.println(request.getParameter("board_id"));
//    System.out.println(request.getParameter("writer"));
//    System.out.println(request.getParameter("comment"));

    String boardId = request.getParameter("board_id");
    String writer = request.getParameter("writer");
    String comment = request.getParameter("comment");

    // 2. DB INSERT
    CommentVO vo = new CommentVO();
    vo.setBoardId(Long.parseLong(boardId));
    vo.setWriter(writer);
    vo.setComment(comment);

    CommentDAO commentDAO = new CommentDAO();
    commentDAO.insertComment(vo);

    // 3. 해당 댓글이 존재하는 게시글로 이동
    response.sendRedirect("view.jsp?seq=" + boardId);
%>