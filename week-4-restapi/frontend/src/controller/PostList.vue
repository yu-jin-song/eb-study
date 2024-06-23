<template>
  <div align="center">
    <h1>자유 게시판 - 목록</h1>

    <!-- 검색 시작 -->
    <Search @search="search" />
    <br />

    <!-- 게시글 전체 갯수 -->
    <p style="text-align: left; padding-left: 250px">
      총 {{ totalPostCount }} 건
    </p>

    <!-- 게시글 목록 -->
    <table
      border="1"
      cellpadding="0"
      cellspacing="0"
      width="700"
      align="center"
    >
      <thead>
        <tr>
          <th bgcolor="orange" width="100">카테고리</th>
          <th bgcolor="orange" width="100">제목</th>
          <th bgcolor="orange" width="100">작성자</th>
          <th bgcolor="orange" width="100">조회수</th>
          <th bgcolor="orange" width="100">등록 일시</th>
          <th bgcolor="orange" width="100">수정 일시</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="post in posts" :key="post.boardId">
          <td>{{ post.categoryName }}</td>
          <td style="text-align: left">
            <span v-if="post.hasFiles">
              <i class="fa-solid fa-paperclip"></i>
            </span>
            <router-link
              :to="{
                name: 'PostView',
                params: { id: post.boardId },
                query: condition,
              }"
            >
              {{ post.title }}
            </router-link>
          </td>
          <td>{{ post.writer }}</td>
          <td>{{ post.views }}</td>
          <td>{{ new Date(post.createdAt).toLocaleString() }}</td>
          <!-- 수정 이력이 없을 경우 '-'로 표기 -->
          <td>
            {{
              post.createdAt === post.modifiedAt
                ? "-"
                : new Date(post.modifiedAt).toLocaleString()
            }}
          </td>
        </tr>
      </tbody>
    </table>
    <!-- 게시글 목록 끝 -->

    <br /><br />

    <!-- 페이징 처리 시작 -->
    <Paging
      :currentPage="condition.currentPage"
      :totalPostCount="totalPostCount"
      @pageChange="handlePageChange"
    />
    <!-- 페이징 처리 끝 -->

    <br /><br />

    <button type="button" @click="goToWritePage">등록</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import Paging from "../components/Paging";
import Search from "../components/Search";
import * as postService from "../services/postService.js";

// variable
/** 게시글 목록 */
const posts = ref([]);

/** 라우터 인스턴스를 가져오는 hook */
const router = useRouter();

/** 게시글 검색 조건 */
const store = useStore();
const condition = ref(store.getters.params);

/** 게시글 전체 갯수 */
const totalPostCount = ref(0);

/**
 * 페이지 변경 이벤트 핸들러
 * @param pageData 페이지 데이터
 */
const handlePageChange = async (pageData) => {
  const { page, pageSize, startIndex } = pageData;

  condition.value.currentPage = page;
  condition.value.postsPerPage = pageSize;
  condition.value.startIndex = startIndex;

  await fetchPostsAndTotalCount();
};

/**
 * 게시글 및 게시글 수를 가져와 상태 업데이트하는 함수
 */
const fetchPostsAndTotalCount = async () => {
  const params = {
    page: condition.value.currentPage,
    startIndex: condition.value.startIndex,
    pageSize: condition.value.postsPerPage,
    selectedCategoryId: condition.value.selectedCategoryId,
    searchKeyword: condition.value.searchKeyword,
    startDate: condition.value.startDate,
    endDate: condition.value.endDate,
  };
  const responseData = await postService.fetchPostsAndTotalCount(params);

  console.log("데이터 확인 : " + JSON.stringify(responseData));
  totalPostCount.value = responseData.totalPostCount;
  posts.value = responseData.boards;
};

/**
 * 검색 버튼을 클릭하여 게시글을 검색하는 함수
 */
const search = async (searchCondition) => {
  condition.value = searchCondition;
  await fetchPostsAndTotalCount();
};

/**
 * 글 작성 페이지로 이동하는 함수
 */
const goToWritePage = () => {
  router.push({ name: "PostWriteForm", query: condition.value });
};

// lifecycle hook
onMounted(async () => {
  await search(condition.value);
});
</script>

<style>
</style>
