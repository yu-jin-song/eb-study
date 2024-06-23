<template>
  <div align="center">
    <h1>게시판 - 보기</h1>

    <!-- 게시글 상세 -->
    <table
      border="1"
      cellpadding="0"
      cellspacing="0"
      width="700"
      align="center"
    >
      <tbody>
        <tr>
          <td>{{ post.writer }}</td>
          <td>{{ "등록일시 " + new Date(post.createdAt).toLocaleString() }}</td>
          <td>
            {{ "수정일시 " + new Date(post.modifiedAt).toLocaleString() }}
          </td>
        </tr>
        <tr>
          <td colspan="2">{{ "[" + post.categoryName + "] " + post.title }}</td>
          <td>{{ post.views }}</td>
        </tr>
        <tr>
          <td colspan="3">{{ post.content }}</td>
        </tr>
        <tr v-if="files.length > 0">
          <td colspan="3" style="text-align: left">
            <!-- 첨부파일이 존재하는 경우 -->
            <div v-for="file in files" :key="file.fileId">
              <i class="fa-solid fa-download"></i>
              <a :href="`/files/download?seq=${file.fileId}`"
                >{{ file.originalName }}.{{ file.ext }}</a
              >
              <br />
            </div>
          </td>
        </tr>
      </tbody>
    </table>
    <!--  게시글 상세 끝  -->

    <PasswordConfirmModal
      @closeModal="showModal = false"
      @modifyPost="modifyPost"
      @deletePost="deletePost"
      :showModal="showModal"
      :id="id"
      :actionType="actionType"
    />

    <br /><br />

    <!-- 댓글 처리 -->
    <form @submit.prevent="submitComment">
      <table
        border="1"
        cellpadding="0"
        cellspacing="0"
        width="700"
        align="center"
      >
        <!-- 댓글 목록 -->
        <tr v-for="comment in comments" :key="comment.commentId">
          <td style="text-align: left; padding-left: 5px">
            <div style="font-weight: bold">{{ comment.writer }}</div>
            <div style="color: darkgrey">
              {{ new Date(comment.createdAt).toLocaleString() }}
            </div>
            <div style="margin-top: 5px; margin-bottom: 5px">
              {{ comment.comment }}
            </div>
          </td>
        </tr>
        <!-- 댓글 목록 끝 -->
        <!-- 댓글 입력 -->
        <tr>
          <td colspan="2" align="left">
            작성자
            <input type="text" v-model="newComment.writer" />
            <br />
            <textarea
              rows="3"
              cols="80"
              v-model="newComment.comment"
              placeholder="댓글을 입력해 주세요."
            >
            </textarea>
            <input type="submit" value="등록" />
          </td>
        </tr>
        <!-- 댓글 입력 끝 -->
      </table>
    </form>
    <!-- 댓글 처리 끝 -->

    <hr />

    <button @click="redirectToList">목록</button>
    <button @click="pushButton('modify')">수정</button>
    <button @click="pushButton('delete')">삭제</button>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useStore } from "vuex";
import PasswordConfirmModal from "../components/PasswordConfirmModal.vue";
import * as postService from "../services/postService.js";
import * as commentService from "../services/commentService.js";

// variable
/** 현재 라우트 정보를 가져오는 hook */
const route = useRoute();

/** 라우터 인스턴스를 가져오는 hook */
const router = useRouter();

/** 게시글 ID */
const id = ref(route.params.id);

/** 게시글 */
const post = ref({});

/** 파일 목록 */
const files = ref([]);

/** 댓글 목록 */
const comments = ref([]);

/** 새 댓글 */
const newComment = ref({
  writer: "",
  comment: "",
});

/** 검색 조건 객체 */
const store = useStore();
const condition = ref(store.getters.params);

/** 모달창 출력 여부 */
const showModal = ref(false);

/** 클릭한 버튼 타입 */
const actionType = ref("");

// function
/**
 * 목록 페이지로 리다이렉트하는 함수
 */
const redirectToList = () => {
  router.push({
    name: "PostList",
    query: condition.value,
  });
};

/**
 * 버튼 클릭 시 액션을 수행하는 함수
 *
 * @param type 버튼 타입
 */
const pushButton = (type) => {
  actionType.value = type;
  showModal.value = true;
};

/**
 * 게시물 수정하는 함수
 */
const modifyPost = () => {
  router.push({
    name: "PostModifyForm",
    params: { id: id.value },
    query: condition.value,
  });
};

/**
 * 게시물 삭제하는 함수
 */
const deletePost = async (password) => {
  try {
    await postService.deletePost(id.value, password);
    alert("게시글이 삭제되었습니다.");
    await router.push({
      name: "PostList",
      query: condition.value,
    });
  } catch (error) {
    alert(error.message);
  }
};

/**
 * 게시글 데이터 가져오는 함수
 */
const fetchPost = async () => {
  const params = {
    page: condition.value.currentPage,
    startIndex: condition.value.startIndex,
    pageSize: condition.value.postsPerPage,
    selectedCategoryId: condition.value.selectedCategoryId,
    searchKeyword: condition.value.searchKeyword,
    startDate: condition.value.startDate,
    endDate: condition.value.endDate,
  };
  const responseData = await postService.fetchPostAndFiles(id.value, params);

  post.value = responseData.board;
  files.value = responseData.files;
};

/**
 * 댓글 작성 폼을 초기화하는 함수
 */
const clearCommentForm = () => {
  newComment.value.writer = "";
  newComment.value.comment = "";
};

/**
 * 댓글 제출 로직
 */
const submitComment = async () => {
  const formData = new FormData();
  formData.append("boardId", id.value);
  formData.append("writer", newComment.value.writer);
  formData.append("comment", newComment.value.comment);

  try {
    await commentService.createComment(id.value, formData);
    await fetchComments();
    clearCommentForm();
  } catch (error) {
    alert(error.message);
  }
};

/**
 * 댓글 목록 가져오기
 */
const fetchComments = async () => {
  comments.value = await commentService.fetchComments(id.value);
};

// lifecycle hook
onMounted(async () => {
  await fetchPost();
  await fetchComments();
});
</script>

<style>
</style>
