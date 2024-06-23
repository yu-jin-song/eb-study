<template>
  <div align="center">
    <h1>게시판 - 등록</h1>

    <hr />

    <!--  게시글 작성  -->
    <form @submit.prevent="submitForm" enctype="multipart/form-data">
      <table border="1" cellpadding="0" cellspacing="0" align="center">
        <tr>
          <td bgcolor="orange" width="70">카테고리*</td>
          <td align="left">
            <select
              v-model="form.categoryId"
              @change="validateField('categoryId')"
              required
            >
              <option value="">카테고리 선택</option>
              <option
                v-for="category in categories"
                :key="category.categoryId"
                :value="category.categoryId"
              >
                {{ category.name }}
              </option>
            </select>
          </td>
        </tr>
        <tr>
          <td bgcolor="orange" width="70">작성자*</td>
          <td align="left">
            <input
              type="text"
              v-model="form.writer"
              @blur="validateField('writer')"
              required
            />
          </td>
        </tr>
        <tr>
          <td bgcolor="orange" width="70">비밀번호*</td>
          <td align="left">
            <input
              type="password"
              v-model="form.password"
              placeholder="비밀번호"
              maxlength="16"
              @blur="validateField('password')"
              required
            />
          </td>
          <td align="left">
            <input
              type="password"
              v-model="form.verifyPassword"
              placeholder="비밀번호 확인"
              maxlength="16"
              @blur="validateField('verifyPassword')"
              required
            />
          </td>
        </tr>
        <tr>
          <td bgcolor="orange" width="70">제목*</td>
          <td align="left">
            <input
              type="text"
              v-model="form.title"
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
              v-model="form.content"
              @blur="validateField('content')"
              required
            ></textarea>
          </td>
        </tr>
        <tr v-for="i in 3" :key="i">
          <td align="left">
            <input type="file" @change="onFileChange" />
          </td>
        </tr>
        <tr>
          <td colspan="2" align="center">
            <button type="button" @click="redirectToList">취소</button>
            <button type="submit">저장</button>
          </td>
        </tr>
      </table>
    </form>
    <!--  게시글 작성 끝  -->
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import * as postService from "../services/postService.js";
import * as categoryService from "../services/categoryService.js";
import useFields from "../composition/useFields.js";
import usePostFormValidation from "../composition/usePostFormValidation";

// variable
/** 뷰 라우터 인스턴스 */
const router = useRouter();

/** 검색 조건 */
const store = useStore();
const condition = ref(store.getters.params);

/** 작성 폼 데이터 */
const form = ref({
  categoryId: "",
  writer: "",
  password: "",
  verifyPassword: "",
  title: "",
  content: "",
  uploadFile: [],
});

/** 카테고리 목록 */
const categories = ref([]);

/** 필드 정보 */
const { fieldValidators } = useFields();

// function
/**
 * 게시글 작성 폼을 제출하는 함수
 */
const onSubmit = async () => {
  // Create FormData object
  const formData = new FormData();
  formData.append("categoryId", form.value.categoryId);
  formData.append("writer", form.value.writer);
  formData.append("password", form.value.password);
  formData.append("title", form.value.title);
  formData.append("content", form.value.content);

  // Append upload files to formData
  if (form.value.uploadFile) {
    form.value.uploadFile.forEach((file) => {
      formData.append("uploadFile", file);
    });
  }

  try {
    // 게시글 등록
    const postId = await postService.createPost(formData);
    alert("게시글이 등록되었습니다.");
    // 게시글 상세 페이지로 이동
    await router.push({
      name: "PostView",
      params: { id: postId },
      query: condition.value,
    });
  } catch (error) {
    alert(error.message);
  }
};

/**
 * 게시글 작성 폼 유효성 검사 및 제출을 처리하는 컴포지션 API를 사용
 * 
 * @property {Function} validateField - 폼 필드의 유효성을 검사하는 함수
 * @property {Function} submitForm - 폼을 제출하는 함수
 */
const { validateField, submitForm } = usePostFormValidation(
  form,
  fieldValidators,
  onSubmit
);

/**
 * 파일 변경 이벤트 처리
 * 
 * @param event
 */
const onFileChange = (event) => {
  const selectedFile = event.target.files[0];
  form.value.uploadFile.push(selectedFile);
};

/**
 * 목록 페이지로 이동
 */
const redirectToList = async () => {
  await router.push({
    name: "PostList",
    query: condition.value,
  });
};

/**
 * 카테고리 목록을 가져오는 함수
 */
const fetchCategories = async () => {
  categories.value = await categoryService.getCategories();
};

// lifecycle hook
onMounted(async () => {
  await fetchCategories();
});
</script>

<style>
/* Add your styles here */
</style>
