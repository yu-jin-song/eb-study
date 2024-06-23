import axios from 'axios';

/**
 * 카테고리 목록 조회 서비스
 * 
 * @returns 카테고리 목록을 담은 배열
 */
export async function getCategories() {
    try {
        const response = await axios.get('/categories');
        return response.data;
    } catch (error) {
        console.error('Error fetching categories:', error);
    }
}