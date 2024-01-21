<%--
  Created by IntelliJ IDEA.
  User: jined
  Date: 2024-01-20
  Time: 오후 2:51
  게시글 전체 목록 조회 페이지
--%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.study.connection.board.dao.BoardDAO"%>
<%@page import="com.study.connection.board.vo.BoardVO"%>
<%@page import="com.study.connection.board.dao.CategoryDAO"%>
<%@page import="com.study.connection.board.vo.CategoryVO"%>

<%
    /* 1. 게시글 및 카테고리 목록 가져오기 */
    BoardVO vo = new BoardVO();
    BoardDAO boardDAO = new BoardDAO();
    List<BoardVO> boardList = null;

    CategoryDAO categoryDAO = new CategoryDAO();
    List<CategoryVO> categoryList = categoryDAO.getCategoryList();

    /* 2. 페이징 처리 */
    int pageSize = 10; // 한 페이지에 출력할 레코드 수

    // 페이지 링크를 클릭한 번호(현재 페이지)
    String pageNum = request.getParameter("pageNum");
    if (pageNum == null) { // 클릭한게 없는경우 1 페이지
        pageNum = "1";
    }
    // 계산을 위한 pageNum 형변환(현재 페이지)
    int currentPage = Integer.parseInt(pageNum);

    // 해당 페이지에서 시작할 레코드와 마지막 레코드
    int startRow = (currentPage - 1) * pageSize + 1;
    int endRow = currentPage * pageSize;

    int totalCnt =  boardDAO.getTotalBoardCount();  // 데이터베이스에 저장된 총 갯수

    if (totalCnt > 0) {
        // getBoardList() 메서드 호출하여 해당 레코드 반환
        boardList = boardDAO.getBoardList(startRow, endRow);
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>글 목록</title>
</head>
<body align="center">

<h1>자유 게시판 - 목록</h1>

<!-- 검색 시작(아직 구현 x) -->
<form action="list.jsp" method="post">
    <table border="1" cellpadding="0" cellspacing="0" width="700" align="center">
        <tr>
            <td align="right">
                <select name="searchCondition">
                    <option value="TITLE">제목
                    <option value="CONTENT">내용
                </select>
                <input type="text" name="searchKeyword"/>
                <input type="text" value="검색"/>
            </td>
        </tr>
    </table>
</form>
<!-- 검색 종료 -->

<br>

<%--게시글 전체 개수--%>
<p style="text-align:left; padding-left: 250px">총 <%=totalCnt%>건</p>

<%--게시글 목록 나타내는 곳--%>
<table border="1" cellpadding="0" cellspacing="0" width="700" align="center">
    <tr>
        <th bgcolor="orange" width="100">카테고리</th>
        <th bgcolor="orange" width="100">제목</th>
        <th bgcolor="orange" width="100">작성자</th>
        <th bgcolor="orange" width="100">조회수</th>
        <th bgcolor="orange" width="100">등록 일시</th>
        <th bgcolor="orange" width="100">수정 일시</th>
    </tr>
    <% for (BoardVO board : boardList) {%>
    <tr>
        <td>
            <%=categoryList.get(board.getCategoryId()-1).getName() %>
        </td>
        <td align="left">
            <a href="view.jsp?seq=<%=board.getId() %>"><%=board.getTitle() %></a>
        </td>
        <td><%=board.getWriter() %></td>
        <td><%=board.getViews() %></td>
        <td><%=board.getCreatedAt() %></td>
        <td><%=board.getModifedAt() %></td>
    </tr>
    <%} %>
</table>
<%--게시글 목록 나타내는 곳 끝--%>

<br />

<%	// 페이징  처리
    if(totalCnt > 0){
        // 총 페이지의 수
        int pageCount = totalCnt / pageSize + (totalCnt%pageSize == 0 ? 0 : 1);
        // 한 페이지에 보여줄 페이지 블럭(링크) 수
        int pageBlock = 10;
        // 한 페이지에 보여줄 시작 및 끝 번호(예 : 1 2 3 ~ 10 / 11 12 13 ~ 20)
        int startPage = ((currentPage-1)/pageBlock)*pageBlock+1;
        int endPage = startPage + pageBlock - 1;

        // 마지막 페이지가 총 페이지 수 보다 크면 endPage를 pageCount로 할당
        if(endPage > pageCount) {
            endPage = pageCount;
        }

        if(startPage > pageBlock) { // 페이지 블록수보다 startPage가 클경우 이전 링크 생성
%>
<%--처음페이지로 이동--%>
<a href="list.jsp?pageNum=1"><<</a>
<%--바로 앞 페이지로 이동--%>
<a href="list.jsp?pageNum=<%=startPage - 10%>"><</a>
<%
    }

    for(int i=startPage; i <= endPage; i++) { // 페이지 블록 번호
        if(i == currentPage) { // 현재 페이지에는 링크를 설정하지 않음
%>
<%--현재 페이지 번호--%>
<%=i %>
<%
        }else{ // 현재 페이지가 아닌 경우 링크 설정
%>
<%--다른 페이지 번호--%>
<a href="list.jsp?pageNum=<%=i%>"><%=i %></a>
<%
        }
    } // for end

    if(endPage < pageCount) { // 현재 블록의 마지막 페이지보다 페이지 전체 블록수가 클경우 다음 링크 생성
%>
<%--바로 다음 페이지로 이동--%>
<a href="list.jsp?pageNum=<%=startPage + 10 %>">></a>
<%--맨 마지막 페이지로 이동--%>
<a href="list.jsp?pageNum=<%=pageCount %>">>></a>
<%
        }
    }
%>

<br><br>

<button type="button" onclick="location.href='write.jsp'">등록</button>

</body>
</html>