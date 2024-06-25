import axios from 'axios';

const BASE_URL = '/boards/free';

/**
 * 게시글 목록과 총 게시글 수 조회 서비스
 * 
 * @param condition - 검색 조건
 * @returns 게시글 목록과 총 게시글 수를 포함한 객체
 */
export async function fetchPostsAndTotalCount(params) {
  try {
    const response = await axios.get(BASE_URL, { params });
    return response.data;
  } catch (error) {
    console.error('Error fetching posts: ', error);
    if (error.response.status === 404) {
      return { totalPostCount: 0, boards: [] };
    }
  }
}

/**
 * 게시글 및 파일 목록 조회 서비스
 * 
 * @param id - 게시글 ID
 * @param condition - 검색 조건
 * @returns 게시글과 파일 목록을 포함한 객체
 */
export async function fetchPostAndFiles(id, params) {
  try {
    const response = await axios.get(`${BASE_URL}/${id}`, { params });
    return response.data;
  } catch (error) {
    console.error('Error fetching posts: ', error);
  }
}

/**
 * 게시글 등록 서비스
 * 
 * @param form 게시글 데이터
 * @returns 새로 생성된 게시글의 ID
 */
export async function createPost(formData) {
  try {
    const response = await axios.post(BASE_URL, formData);
    return response.data;
  } catch (error) {
    console.error('Error checking password:', error);

    let message = '서버 에러 발생';
    if (error.response.status === 400) {
      message = '잘못된 요청입니다.';
    }
    throw new Error(message);
  }
}

/**
 * 게시글 수정 서비스
 * 
 * @param post - 수정할 게시글 데이터
 */
export async function modifyPost(formData) {
  try {
    await axios.put(BASE_URL, formData);
  } catch (error) {
    console.error('Error modifying post:', error);

    let message = '서버 에러 발생';
    if (error.response.status === 401) {
      message = '비밀번호가 틀립니다.';
    }
    throw new Error(message);
  }
}

/**
 * 게시글 삭제 서비스
 * 
 * @param id - 삭제할 게시글의 ID
 */
export async function deletePost(id, password) {
  try {
    // 헤더 생성
    const headers = {
      headers: {
        'X-Post-Password': password,
      }
    };

    await axios.delete(`${BASE_URL}/${id}`, headers);
  } catch (error) {
    console.error('Error deleting post:', error);
    throw new Error('게시물 삭제 중 오류가 발생했습니다.');
  }
}

/**
 * 비밀번호 확인 서비스
 * 
 * @param boardId 게시글 ID
 * @param password 비밀번호
 */
export async function checkPassword(boardId, password) {
  try {
    await axios.post(`${BASE_URL}/check-password`, {
      boardId,
      password
    });
  } catch (error) {
    console.error('Error checking password:', error);

    let message = '비밀번호 확인 중 오류가 발생했습니다.';
    if (error.response.status === 401) {
      message = '비밀번호가 틀립니다.';
    }
    throw new Error(message);
  }
}