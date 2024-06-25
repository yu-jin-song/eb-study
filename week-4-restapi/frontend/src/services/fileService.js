import axios from 'axios';

const BASE_URL = '/files';

/**
 * 파일 다운로드 서비스
 * 
 * @param fileId - 파일 ID
 * @returns 다운로드된 파일 데이터를 담은 Blob 객체
 */
export async function downloadFile(fileId) {
  try {
    const response = await axios.get(
      `${BASE_URL}/download?seq=${fileId}`,
      { responseType: 'blob' }
    );
    return response.data;
  } catch (error) {
    console.error('Error downloading file: ', error);
  }
}

/**
 * 파일 삭제 서비스
 * 
 * @param fileId - 삭제할 파일의 ID
 * @throws 파일 삭제 중 오류가 발생했을 때 에러
 */
export async function deleteFile(fileId) {
  try {
    await axios.delete(`${BASE_URL}/${fileId}`);
  } catch (error) {
    console.error('Error deleting post:', error);
    throw new Error('파일 삭제 중 오류가 발생했습니다.');
  }
}