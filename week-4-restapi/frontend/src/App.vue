<template>
  <router-view />
</template>

<script setup>
import { onBeforeMount } from "vue";
import { useRouter } from "vue-router";

// router 인스턴스 생성
const router = useRouter();

// created 라이프사이클 훅 대신 onBeforeMount 사용
onBeforeMount(async () => {
  // App.vue가 생성될 때 초기 경로 설정
  await router.push({ name: "PostList" });
});
</script>


<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
