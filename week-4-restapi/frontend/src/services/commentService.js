import axios from 'axios';

const BASE_URL = '/comments';

/**
 * 새로운 댓글 생성 서비스
 * 
 * @param id - 게시글 ID
 * @param comment - 댓글 객체
 * @throws 댓글 생성 중 오류 발생 시 에러
 */
export async function createComment(id, comment) {
    const formData = new FormData();
    formData.append('boardId', id);
    formData.append('writer', comment.writer);
    formData.append('comment', comment.comment);

    try {
        await axios.post(BASE_URL, formData);
    } catch (error) {
        console.error('Error submitting comment:', error);
        throw new Error('게시물 삭제 중 오류가 발생했습니다.');
    }
}

/**
 * 댓글 조회 서비스
 * 
 * @param id - 게시글 ID
 * @returns 댓글 목록을 담은 배열
 */
export async function fetchComments(id) {
    try {
        const response = await axios.get(`${BASE_URL}?boardId=${id}`);
        return response.data;
    } catch (error) {
        console.error('Error fetching comments: ', error);
    }
}