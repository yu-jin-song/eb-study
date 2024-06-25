package com.study.board.controller;

import com.mysql.cj.util.StringUtils;
import com.study.board.dao.BoardDAO;
import com.study.board.dao.CategoryDAO;
import com.study.board.util.Condition;
import com.study.board.vo.BoardVO;
import com.study.board.vo.CategoryVO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 게시판 목록을 보여주는 커맨드를 구현한 클래스입니다.
 * 이 클래스는 사용자의 요청에 따라 게시판의 목록을 조회하고 결과를 list.jsp 페이지에 전달합니다.
 */
public class BoardListCommand implements Command {

    /**
     * 게시판 목록 조회 요청을 처리합니다.
     * 사용자가 제공한 검색 조건과 페이징 정보를 바탕으로 게시판의 목록을 조회하고,
     * 조회 결과를 list.jsp 페이지에 전달합니다.
     *
     * @param request  클라이언트의 요청 정보를 담고 있는 HttpServletRequest 객체
     * @param response 클라이언트에게 응답을 보내는 HttpServletResponse 객체
     * @throws IOException 요청 또는 응답 처리 중 입출력 예외가 발생할 경우
     * @throws ServletException 요청 처리 중 발생하는 예외
     */
    public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        BoardDAO boardDAO = new BoardDAO();
        List<BoardVO> boardList = new ArrayList<>();

        /* 2. 검색 조건 추출*/
        Condition condition = new Condition();

//        String startDateStr = request.getParameter("startDate");
//        String endDateStr = request.getParameter("endDate");
//        String selectedCategory = request.getParameter("category").replaceAll(" ", "");
        String selectedCategory = request.getParameter("category");
        String keyword = request.getParameter("searchKeyword");

        // 2-1. 시작 ~ 종료 날짜 타입 변환
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date parsedDate = null;
//        Timestamp startDate = null;
//        Timestamp endDate = null;
//
//        try {
//            parsedDate = dateFormat.parse(startDateStr);
//            startDate = new Timestamp(parsedDate.getTime());
//
//            parsedDate = dateFormat.parse(endDateStr);
//            endDate = new Timestamp(parsedDate.getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        // 2-2. 선택된 카테고리(전체 카테고리 : -1)
        int selectedCategoryId = (StringUtils.isNullOrEmpty(selectedCategory)) ? -1 : Integer.parseInt(selectedCategory);
//        System.out.println("startDate = " + startDate + ", endDate = " + endDate + ", selectedCategoryId = " + selectedCategoryId + ", keyword = " + keyword);

        condition.setCategoryId(selectedCategoryId);
        condition.setKeyword(keyword);

        /* 1. 페이징 처리 및 게시글 목록 가져오기 */
        int pageSize = 10; // 한 페이지에 출력할 레코드 수

        // 페이지 링크를 클릭한 번호(현재 페이지)
        String pageNum = request.getParameter("page_num");
        if (StringUtils.isNullOrEmpty(pageNum)) { // 클릭한게 없거나 조 1 페이지
            pageNum = "1";
        }

        int totalCnt = boardDAO.getBoardCount(condition);  // 데이터베이스에 저장된 게시글 갯수

        // 계산을 위한 pageNum 형변환(현재 페이지)
        int currentPage = Integer.parseInt(pageNum);

        // 해당 페이지에서 시작할 레코드와 마지막 레코드
        int startRow = (currentPage - 1) * pageSize + 1;
        int endRow = currentPage * pageSize;

        condition.setStartRow(startRow);
        condition.setEndRow(endRow);

        if (totalCnt > 0) {
            // getBoardList() 메서드 호출하여 해당 레코드 반환
//            boardList = boardDAO.getBoardList(startRow, endRow);
//            boardList = boardDAO.getBoardList(startRow, endRow, startDate, endDate);
            boardList = boardDAO.getBoardList(condition);
//            boardList = boardDAO.getBoardList(startRow, endRow, startDate, endDate, selectedCategoryId, keyword);
        }

        /* 3. 카테고리 목록 가져오기 */
        CategoryDAO categoryDAO = new CategoryDAO();
        List<CategoryVO> categoryList = categoryDAO.getCategoryList();

        request.setAttribute("category_id", selectedCategoryId);
        request.setAttribute("keyword", keyword);
        request.setAttribute("total_cnt", totalCnt);
        request.setAttribute("current_page", currentPage);
        request.setAttribute("page_size", pageSize);
        request.setAttribute("board_list", boardList);
        request.setAttribute("category_list", categoryList);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/list.jsp");
        dispatcher.forward(request, response);


    }
}
