<%--
  Created by IntelliJ IDEA.
  User: jined
  Date: 2024-01-20
  Time: 오후 2:51
  게시글 전체 목록 조회 페이지
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.study.board.vo.BoardVO" %>
<%@ page import="com.study.board.vo.CategoryVO" %>
<%@ page import="com.mysql.cj.util.StringUtils" %>

<%
    int selectedCategoryId = (int) request.getAttribute("category_id");
    String keyword = (String) request.getAttribute("keyword");
    int totalCnt = (int) request.getAttribute("total_cnt");
    int currentPage = (int) request.getAttribute("current_page");
    int pageSize = (int) request.getAttribute("page_size");
    List<BoardVO> boardList = (List<BoardVO>) request.getAttribute("board_list");
    List<CategoryVO> categoryList = (List<CategoryVO>) request.getAttribute("category_list");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>글 목록</title>
    </head>
    <body align="center">

        <h1>자유 게시판 - 목록</h1>

        <!-- 검색 시작 -->
        <form action="list.do" method="get">
            <table border="1" cellpadding="0" cellspacing="0" width="700" align="center">
                <tr>
                    <td align="left">
                        등록일 : <input type="date" id="startDate" name="startDate" value="" /> ~ <input type="date" id="endDate" name="endDate" value="" />
                    </td>
                    <td align="right">
                        <select name="category">
                            <option value="" <%= (selectedCategoryId == -1) ? "selected" : "" %>>전체 카테고리</option>
                        <% for (CategoryVO category : categoryList) {
                            String selected = (category.getId() == selectedCategoryId) ? "selected" : "";
                        %>
                            <option value="<%= category.getId() %>" <%= selected %>><%= category.getName() %></option>
                        <% } %>
                        </select>
                        <input type="text" name="searchKeyword" placeholder="검색어를 입력해 주세요. (제목 + 작성자 + 내용)" value="<%= (StringUtils.isNullOrEmpty(keyword)) ? "" : keyword %>"/>
                        <input type="submit" value="검색" />
                    </td>
                </tr>
            </table>
        </form>
        <!-- 검색 종료 -->

        <br>

        <%--게시글 전체 개수--%>
        <p style="text-align: left; padding-left: 250px">총 <%= totalCnt %>건</p>

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
            <% for (BoardVO board : boardList) { %>
            <tr>
                <td><%= categoryList.get(board.getCategoryId() - 1).getName() %></td>
        <%--        <td><%= board.getCategoryId() %></td>--%>
                <td align="left">
                    <a href="../view.do?seq=<%= board.getId() %>"><%= board.getTitle() %></a>
                </td>
                <td><%= board.getWriter() %></td>
                <td><%= board.getViews() %></td>
                <td><%= board.getCreatedAt() %></td>
                <td><%= board.getModifedAt() %></td>
            </tr>
            <% } %>
        </table>
        <%--게시글 목록 나타내는 곳 끝--%>

        <%	// 페이징  처리
            if (totalCnt > 0) {
                String searchParam = "";
                if (selectedCategoryId != -1) {
                    searchParam += "&category=" + selectedCategoryId;
                }
                if (!StringUtils.isNullOrEmpty(keyword)) {
                    searchParam += "&searchKeyword=" + keyword;
                }

                // 총 페이지의 수
                int pageCount = totalCnt / pageSize + (totalCnt % pageSize == 0 ? 0 : 1);
                // 한 페이지에 보여줄 페이지 블럭(링크) 수
                int pageBlock = 10;
                // 한 페이지에 보여줄 시작 및 끝 번호(예 : 1 2 3 ~ 10 / 11 12 13 ~ 20)
                int startPage = ((currentPage - 1) / pageBlock) * pageBlock + 1;
                int endPage = startPage + pageBlock - 1;

                // 마지막 페이지가 총 페이지 수 보다 크면 endPage를 pageCount로 할당
                if (endPage > pageCount) {
                    endPage = pageCount;
                }

                if (startPage > pageBlock) { // 페이지 블록수보다 startPage가 클경우 이전 링크 생성
        %>
        <%--처음페이지로 이동--%>
        <a href="list.do?page_num=1<%= searchParam %>"><<</a>
        <%--바로 앞 페이지로 이동--%>
        <a href="list.do?page_num=<%= startPage - 10 %><%= searchParam %>"><</a>
        <%
            }

            for (int i = startPage; i <= endPage; i++) { // 페이지 블록 번호
                if (i == currentPage) { // 현재 페이지에는 링크를 설정하지 않음
        %>
        <%--현재 페이지 번호--%>
        <%= i %>
        <%
                } else { // 현재 페이지가 아닌 경우 링크 설정
        %>
        <%--다른 페이지 번호--%>
        <a href="list.do?page_num=<%= i %><%= searchParam %>"><%= i %></a>
        <%
                }
            } // for end

            if (endPage < pageCount) { // 현재 블록의 마지막 페이지보다 페이지 전체 블록수가 클경우 다음 링크 생성
        %>
        <%--바로 다음 페이지로 이동--%>
        <a href="list.do?page_num=<%= startPage + 10 %><%= searchParam %>">></a>
        <%--맨 마지막 페이지로 이동--%>
        <a href="list.do?page_num=<%= pageCount %><%= searchParam %>">>></a>
        <%
                }
            }
        %>

        <br><br>

        <button type="button" onclick="location.href='writeForm.do'">등록</button>

        <script type="text/javascript">
            // ====== 검색 날짜 셋팅(등록일 기준)
            // 1. 현재 날짜 구하기
            const currentDate = new Date();

            // 2. 시작 일자 설정
            let startDate = new Date(currentDate.getTime());
            startDate.setFullYear(startDate.getFullYear() - 1);
            startDate = startDate.toISOString().substring(0, 10);

            // 3. 종료 일자 설정
            let endDate = new Date(currentDate.getTime());
            endDate = endDate.toISOString().substring(0, 10);

            document.getElementById('startDate').value = startDate;
            document.getElementById('endDate').value = endDate;
            // ====== 검색 날짜 셋팅 끝
        </script>
    </body>
</html>