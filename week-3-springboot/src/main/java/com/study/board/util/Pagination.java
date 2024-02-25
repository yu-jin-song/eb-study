package com.study.board.util;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO : view로 분리
 * 페이징을 관리하는 클래스
 */
@Getter
@Setter
public class Pagination {

    // 페이지 당 보여지는 게시글의 최대 개수
    private int pageSize = 10;

    // 페이징된 버튼의 블럭당 최대 개수
    private int blockSize = 10;

    // 현재 페이지
    private int page = 1;

    // 현재 블럭
    private int block = 1;

    // 총 게시글 수
    private int totalPostCount;

    // 총 페이지 수
    private int totalPageCount;

    // 총 블럭 수
    private int totalBlockCount;

    // 블럭 시작 페이지
    private int startPage = 1;

    // 블럭 마지막 페이지
    private int endPage = 1;

    // DB 접근 시작 index
    private int startIndex = 0;

    // 이전 블럭의 마지막 페이지
    private int prevBlock;

    // 다음 블럭의 시작 페이지
    private int nextBlock;


    /**
     * 페이징 관련 데이터 초기화
     *
     * @param totalPostCount 총 게시물 수
     * @param page           현재 페이지
     */
    public Pagination(int totalPostCount, int page) {
        /* 현재 페이지 */
        this.page = page;

        /* 총 게시글 수 */
        this.totalPostCount = totalPostCount;

        /* 총 페이지 수 */
        // 한 페이지의 최대 개수를 총 게시물 수 * 1.0로 나누어주고 올림 해준다.
        // 총 페이지 수 를 구할 수 있다.
        this.totalPageCount = (int) Math.ceil(totalPostCount * 1.0 / this.pageSize);

        /* 총 블럭 수 */
        // 한 블럭의 최대 개수를 총  페이지의 수 * 1.0로 나누어주고 올림 해준다.
        // 총 블럭 수를 구할 수 있다.
        this.totalBlockCount = (int) Math.ceil(this.totalPageCount * 1.0 / this.pageSize);

        /* 현재 블럭 */
        // 현재 페이지 * 1.0을 블록의 최대 개수로 나누어주고 올림한다.
        // 현재 블록을 구할 수 있다.
        this.block = (int) Math.ceil((page * 1.0) / this.blockSize);

        /* 블럭 시작 페이지 */
        this.startPage = (this.block - 1) * this.blockSize + 1;

        /* 블럭 마지막 페이지 */
        this.endPage = this.startPage + this.blockSize - 1;

        /* === 블럭 마지막 페이지에 대한 validation ===*/
        if (this.endPage > this.totalPageCount) {
            this.endPage = this.totalPageCount;
        }

        /* 이전 블럭(클릭 시, 이전 블럭 마지막 페이지) */
        this.prevBlock = (this.block * this.blockSize) - this.blockSize;

        /* === 이전 블럭에 대한 validation === */
        if (this.prevBlock < 1) {
            this.prevBlock = 1;
        }

        /* 다음 블럭(클릭 시, 다음 블럭 첫번째 페이지) */
        this.nextBlock = (this.block * this.blockSize) + 1;

        /* === 다음 블럭에 대한 validation ===*/
        if (this.nextBlock > totalPageCount) {
            this.nextBlock = totalPageCount;
        }

        /* DB 접근 시작 index */
        this.startIndex = (page - 1) * this.pageSize;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "pageSize=" + pageSize +
                ", blockSize=" + blockSize +
                ", page=" + page +
                ", block=" + block +
                ", totalPostCount=" + totalPostCount +
                ", totalPageCount=" + totalPageCount +
                ", totalBlockCount=" + totalBlockCount +
                ", startPage=" + startPage +
                ", endPage=" + endPage +
                ", startIndex=" + startIndex +
                ", prevBlock=" + prevBlock +
                ", nextBlock=" + nextBlock +
                '}';
    }
}
