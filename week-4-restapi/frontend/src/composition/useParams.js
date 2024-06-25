import { ref } from 'vue';


/**
 * 현재 라우트의 쿼리 파라미터를 파싱하여 반환하는 함수
 * 
 * @returns 파싱된 쿼리 파라미터 객체
 */
export default function useParams(condition) {
  const params = ref({
    currentPage: 1, // 현재 페이지 번호
    startIndex: 0,  // 시작 인덱스
    postsPerPage: 10, // 페이지당 게시물 수
    startDate: '',  // 검색 시작 날짜
    endDate: '',  // 검색 종료 날짜
    selectedCategoryId: '', // 선택된 카테고리의 ID
    searchKeyword: '' // 검색 키워드
  });

  if (condition) {
    params.value = JSON.parse(condition);
  }

  return params.value;
}
