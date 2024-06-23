import { createStore } from 'vuex';

export default createStore({
  state: {
    /** 게시글 조회 및 검색에 사용되는 파라미터 */
    params: {
      currentPage: 1,
      startIndex: 0,
      postsPerPage: 10,
      startDate: '',
      endDate: '',
      selectedCategoryId: '',
      searchKeyword: ''
    }
  },
  mutations: {
    /**
     * 게시글 조회 및 검색 파라미터를 업데이트하는 뮤테이션
     * 
     * @param {Object} state - 현재 상태 객체
     * @param {Object} params - 업데이트할 파라미터 객체
     */
    updateParams(state, params) {
      state.params = { ...state.params, ...params };
    }
  },
  actions: {
    /**
     * 게시글 조회 및 검색 파라미터를 업데이트하는 액션
     * 
     * @param {Function} commit - 뮤테이션 호출 함수
     * @param {Object} params - 업데이트할 파라미터 객체
     */
    updateParams({ commit }, params) {
      commit('updateParams', params);
    }
  },
  getters: {
    /**
     * 게시글 조회 및 검색 파라미터를 반환하는 게터
     * 
     * @param {Object} state - 현재 상태 객체
     * @returns {Object} - 게시글 조회 및 검색 파라미터
     */
    params: state => state.params
  }
});