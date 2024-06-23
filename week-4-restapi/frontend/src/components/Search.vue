<template>
  <div>
    <input type="date" v-model="condition.startDate" />
    ~
    <input type="date" v-model="condition.endDate" />
    <select v-model="condition.selectedCategoryId">
      <option value="">전체 카테고리</option>
      <option
        v-for="category in categories"
        :value="category.categoryId"
        :key="category.categoryId"
      >
        {{ category.name }}
      </option>
    </select>
    <input
      type="text"
      v-model="condition.searchKeyword"
      placeholder="검색어를 입력해 주세요. (제목 + 작성자 + 내용)"
    />
    <button @click="search">검색</button>
  </div>
</template>

<script setup>
import { ref, defineEmits, onMounted } from "vue";
import { useStore } from "vuex";
import * as categoryService from "../services/categoryService";

/**
 * 검색 이벤트를 정의하는 emits 함수
 */
const emits = defineEmits(["search"]);

/** 검색 조건 상태 */
const store = useStore();
const condition = ref(store.getters.params);

/** 카테고리 목록 */
const categories = ref([]);

// function
/**
 * 카테고리 목록을 가져와 상태 업데이트하는 함수
 */
const fetchCategories = async () => {
  categories.value = await categoryService.getCategories();
};

/**
 * 검색 버튼을 클릭하여 게시글을 검색하는 함수
 */
const search = async () => {
  emits("search", condition.value);
};

/**
 * 검색 기간을 초기화하는 함수
 */
const initSearchPeriod = () => {
  const currentDate = new Date();
  condition.value.endDate = currentDate.toISOString().substr(0, 10);
  const startDate = new Date(currentDate.getTime());
  startDate.setFullYear(startDate.getFullYear() - 1);
  condition.value.startDate = startDate.toISOString().substr(0, 10);
};

// lifecycle hook
onMounted(async () => {
  initSearchPeriod();
  await fetchCategories();
});
</script>

<style>
</style>