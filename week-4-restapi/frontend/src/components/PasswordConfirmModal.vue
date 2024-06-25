<template>
  <div class="black-bg" v-if="showModal">
    <div class="white-bg">
      <table border="1" cellpadding="0" cellspacing="0" align="center">
        <tr>
          <td bgcolor="orange" width="70">비밀번호*</td>
          <td align="left">
            <input
              type="password"
              placeholder="비밀번호를 입력해 주세요."
              v-model="password"
            />
          </td>
        </tr>
      </table>
      <button @click="$emit('closeModal')">취소</button>
      <button @click="confirmAction">확인</button>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from "vue";
import * as postService from "../services/postService.js";

/**
 * 컴포넌트에 전달되는 속성을 정의하는 props 객체
 * 
 * @property {boolean} showModal 모달 표시 여부
 * @property {number} id 게시글 ID
 * @property {string} actionType 액션 타입
 */
const props = defineProps({
  showModal: Boolean,
  id: Number,
  actionType: String,
});

/** 비밀번호 */
const password = ref("");

/**
 * 게시글 수정/삭제 이벤트를 정의하는 emits 함수
 */
const emits = defineEmits(["modifyPost", "deletePost"]);

/**
 * 비밀번호 확인 후 액션 수행하는 함수
 */
const confirmAction = async () => {
  try {
    await postService.checkPassword(props.id, password.value);
    emits(props.actionType + "Post", password.value);
  } catch (error) {
    alert(error.message);
  }
};
</script>

<style>
body {
  margin: 0;
}
div {
  box-sizing: border-box;
}
.black-bg {
  width: 100%;
  height: 25%;
  background: rgba(0, 0, 0, 0.5);
  position: fixed;
  padding: 20px;
}
.white-bg {
  width: 100%;
  background: white;
  border-radius: 8px;
  padding: 20px;
}
</style>