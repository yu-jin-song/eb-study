<template>
  <!-- 페이징 처리 시작 -->
  <div align="center">
    <button @click="prevPage" :disabled="startPage <= 1">이전</button>
    <button
      v-for="pageNumber in displayedPages"
      :key="pageNumber"
      @click="goToPage(pageNumber)"
      :class="{ active: pageNumber === currentPage }"
    >
      {{ pageNumber }}
    </button>
    <button @click="nextPage" :disabled="endPage === totalPageCount">
      다음
    </button>
  </div>
  <!-- 페이징 처리 끝 -->
</template>

<script setup>
import { defineProps, defineEmits, ref, computed } from "vue";

/**
 * 컴포넌트에 전달되는 속성을 정의하는 props 객체
 * 
 * @property {number} currentPage 현재 페이지 번호
 * @property {number} totalPostCount 전체 게시물 수
 */
const props = defineProps({
  currentPage: Number,
  totalPostCount: Number,
});

/**
 * 페이지 변경 이벤트를 정의하는 emits 함수
 */
const emits = defineEmits(["pageChange"]);

/** 한 페이지에 출력할 번호 갯수 */
const displayPageNumber = ref(10);

/**
 * 게시글의 총 페이지 수를 계산하는 computed 속성
 *
 * @return {number} 총 페이지 수
 */
const totalPageCount = computed(() => {
  return Math.ceil(props.totalPostCount / displayPageNumber.value);
});

/**
 * 현재 화면에서 시작할 페이지 번호를 계산하는 computed 속성
 *
 * @return {number} 시작 페이지 번호
 */
const startPage = computed(() => {
  return Math.max(
    1,
    Math.floor((props.currentPage - 1) / displayPageNumber.value) *
      displayPageNumber.value + 1
  );
});

/**
 * 현재 화면에서 끝날 페이지 번호를 계산하는 computed 속성
 *
 * @return {number} 끝 페이지 번호
 */
const endPage = computed(() => {
  let end = startPage.value + displayPageNumber.value - 1;
  return Math.min(totalPageCount.value, end);
});

/**
 * 현재 화면에 표시할 페이지 번호 목록을 계산하는 computed 속성
 *
 * @return {Array<number>} 페이지 번호 목록
 */
const displayedPages = computed(() => {
  const pages = [];
  for (let page = startPage.value; page <= endPage.value; page++) {
    pages.push(page);
  }
  return pages;
});

/**
 * 페이지 번호에 해당하는 시작 인덱스를 계산하는 함수
 *
 * @param {number} pageNumber 페이지 번호
 * @returns {number} 시작 인덱스
 */
const calculateStartIndex = (pageNumber) => {
  return (pageNumber - 1) * displayPageNumber.value;
};

/**
 * 페이지 변경 이벤트를 발생시키는 함수
 *
 * @param {number} pageNumber 변경할 페이지 번호
 */
const emitPageChange = (pageNumber) => {
  emits("pageChange", {
    page: pageNumber,
    pageSize: displayPageNumber.value,
    startIndex: calculateStartIndex(pageNumber),
  });
};

/**
 * 이전 페이지로 이동하는 함수
 */
const prevPage = () => {
  if (startPage.value > 1) {
    emitPageChange(startPage.value - 1);
  }
};

/**
 * 다음 페이지로 이동하는 함수
 */
const nextPage = () => {
  if (endPage.value < props.totalPages) {
    emitPageChange(endPage.value + 1);
  }
};

/**
 * 특정 페이지로 이동하는 함수
 *
 * @param {number} pageNumber 이동할 페이지 번호
 */
const goToPage = (pageNumber) => {
  emitPageChange(pageNumber);
};
</script>

<style>
</style>