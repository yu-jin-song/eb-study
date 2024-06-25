<template>
  <div align="center">
    <h1>게시판 - 수정</h1>

    <hr />

    <!--  게시글 수정  -->
    <form @submit.prevent="submitForm" enctype="multipart/form-data">
      <table border="1" cellpadding="0" cellspacing="0" align="center">
        <tr>
          <td bgcolor="orange" width="70">카테고리*</td>
          <td align="left">{{ post.categoryName }}</td>
        </tr>
        <tr>
          <td bgcolor="orange" width="70">등록 일시</td>
          <td align="left">
            {{ new Date(post.createdAt).toLocaleString() }}
          </td>
        </tr>
        <tr>
          <td bgcolor="orange" width="70">수정 일시</td>
          <td align="left">
            {{ new Date(post.modifiedAt).toLocaleString() }}
          </td>
        </tr>
        <tr>
          <td bgcolor="orange" width="70">조회수</td>
          <td align="left">{{ post.views }}</td>
        </tr>
        <tr>
          <td bgcolor="orange" width="70">작성자*</td>
          <td align="left">
            <input
              type="text"
              v-model="post.writer"
              @blur="validateField('writer')"
              required
            />
          </td>
        </tr>
        <tr>
          <td bgcolor="orange" width="70">비밀번호</td>
          <td align="left">
            <input
              type="password"
              v-model="post.password"
              placeholder="비밀번호"
              maxlength="16"
              @blur="validateField('password')"
              required
            />
          </td>
        </tr>
        <tr>
          <td bgcolor="orange" width="70">제목*</td>
          <td align="left">
            <input
              type="text"
              v-model="post.title"
              @blur="validateField('title')"
              required
            />
          </td>
        </tr>
        <tr>
          <td bgcolor="orange">내용*</td>
          <td align="left">
            <textarea
              rows="10"
              cols="40"
              v-model="post.content"
              @blur="validateField('content')"
              required
            ></textarea>
          </td>
        </tr>
        <tr v-for="file in files" :key="file.fileId">
          <td colspan="2" align="left">
            <span>{{ file.originalName }}.{{ file.ext }}</span>
            <button
              type="button"
              @click="
                downloadFile(file.fileId, file.originalName + '.' + file.ext)
              "
            >
              Download
            </button>
            <button type="button" @click="deleteFile(file.fileId)">
              Delete
            </button>
          </td>
        </tr>
        <tr v-for="index in missingFilesCount" :key="index">
          <td colspan="2" align="left">
            <input type="file" @change="onFileChange" />
          </td>
        </tr>
        <tr>
          <td colspan="2" align="center">
            <button type="button" @click="redirectToView">취소</button>
            <button type="submit" id="saveButton">저장</button>
          </td>
        </tr>
      </table>
    </form>
    <!--  게시글 수정 끝  -->
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useStore } from "vuex";
import * as postService from "../services/postService.js";
import * as fileService from "../services/fileService.js";
import useFields from "../composition/useFields.js";
import usePostFormValidation from "../composition/usePostFormValidation.js";

// variable
/** 현재 라우트 정보를 가져오는 hook */
const route = useRoute();

/** 라우터 인스턴스를 가져오는 hook */
const router = useRouter();

/** 게시글 */
const post = ref({
  boardId: route.params.id, // 게시글 ID
  categoryName: "", // 카테고리명
  createdAt: "", // 등록 일시
  modifiedAt: "", // 수정 일시
  views: "", // 조회수
  writer: "", // 작성자
  title: "", // 제목
  content: "", // 내용
  password: "", // 비밀번호
  uploadFile: [], // 업로드 할 파일 목록
});

/** 파일 목록 */
const files = ref([]);

/** 검색 조건 */
const store = useStore();
const condition = ref(store.getters.params);

/** 필드 정보를 가져오는 getter */
const { filteredFields } = useFields();

// computed 속성
/**
 * 존재하지 않는 파일 갯수 계산
 *
 * @return 존재하지 않는 파일 갯수
 */
const missingFilesCount = computed(() => {
  return Math.max(0, 3 - files.value.length);
});

// function
/**
 * 폼 제출 이벤트 핸들러
 */
const onSubmit = async () => {
  try {
    await postService.checkPassword(post.value.boardId, post.value.password);
    await modifyPost();
  } catch (error) {
    alert(error.message);
  }
};

/**
 * 유효성 검사와 폼 제출 로직을 가져오는 hook
 */
const { validateField, submitForm } = usePostFormValidation(
  post,
  filteredFields(),
  onSubmit
);

/**
 * 게시글을 수정하는 함수
 */
const modifyPost = async () => {
  // Create FormData object
  const formData = new FormData();
  formData.append("boardId", post.value.boardId);
  formData.append("writer", post.value.writer);
  formData.append("title", post.value.title);
  formData.append("content", post.value.content);
  formData.append("password", post.value.password);

  // Append upload files to formData
  if (post.value.uploadFile) {
    post.value.uploadFile.forEach((file) => {
      formData.append("uploadFile", file);
    });
  }

  try {
    await postService.modifyPost(formData);
    alert("게시글이 수정되었습니다.");
    await router.push({
      name: "PostView",
      params: { id: post.value.boardId },
      query: condition.value,
    });
  } catch (error) {
    alert(error.message);
  }
};

/**
 * 파일 변경 이벤트 핸들러
 * @param event 파일 변경 이벤트
 */
const onFileChange = (event) => {
  const selectedFile = event.target.files[0];
  if (!post.value.uploadFile) {
    post.value.uploadFile = [];
  }
  post.value.uploadFile.push(selectedFile);
};

/**
 * 파일 다운로드 함수
 * @param fileId 파일 ID
 * @param fileName 파일 이름
 */
const downloadFile = async (fileId, fileName) => {
  const responseData = await fileService.downloadFile(fileId);

  const url = window.URL.createObjectURL(new Blob([responseData]));
  const link = document.createElement("a");

  link.href = url;
  link.setAttribute("download", fileName);
  link.click();
};

/**
 * 파일 삭제 함수
 *
 * @param fileId - 파일 ID
 */
const deleteFile = async (fileId) => {
  try {
    await fileService.deleteFile(fileId);

    const index = files.value.findIndex((file) => file.fileId === fileId);
    if (index >= 0) {
      files.value.splice(index, 1);
    }
  } catch (error) {
    alert(error.message);
  }
};

/**
 * 게시글 상세 페이지로 이동하는 함수
 */
const redirectToView = () => {
  router.push({
    name: "PostView",
    params: { id: post.value.boardId },
    query: condition.value,
  });
};

/**
 * 게시글 및 파일목록을 가져오는 함수
 */
const fetchPostAndFiles = async () => {
  const params = {
    page: condition.value.currentPage,
    startIndex: condition.value.startIndex,
    pageSize: condition.value.postsPerPage,
    selectedCategoryId: condition.value.selectedCategoryId,
    searchKeyword: condition.value.searchKeyword,
    startDate: condition.value.startDate,
    endDate: condition.value.endDate,
  };
  const responseData = await postService.fetchPostAndFiles(
    post.value.boardId,
    params
  );

  post.value = responseData.board;
  files.value = responseData.files;
};

// lifecycle hook
onMounted(async () => {
  await fetchPostAndFiles();
});
</script>

<style>
/* Add your styles here */
</style>
