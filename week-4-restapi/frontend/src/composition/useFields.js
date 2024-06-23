/**
 * 사용자 입력 필드에 대한 유효성 검사 로직을 정의하는 훅
 * 
 * @returns 유효성 검사 조건, 필드를 필터링한 객체
 */
export default function useFields() {
    /** 필드별 유효성 검사 조건 */
    const fieldValidators = {
        categoryId: {
            validator: value => value !== '' && value !== undefined,
            message: '카테고리를 선택해주세요.'
        },
        writer: {
            maxLength: 4,
            validator: value => value.length >= 3 && value.length < 5,
            message: '작성자는 3글자 이상, 5글자 미만이어야 합니다.'
        },
        password: {
            maxLength: 15,
            validator: value => /^(?!.*\s)(?=.*[a-zA-Z])(?=.*\d)(?=.*[\W_]).{4,15}$/.test(value),
            message: '비밀번호는 4글자 이상, 16글자 미만이며, 영문, 숫자, 특수문자를 포함해야 합니다.'
        },
        verifyPassword: {
            validator: (value, password) => value === password,
            message: '비밀번호가 일치하지 않습니다.'
        },
        title: {
            maxLength: 99,
            validator: value => value.length >= 4 && value.length < 100,
            message: '제목은 4글자 이상, 100글자 미만이어야 합니다.'
        },
        content: {
            maxLength: 1999,
            validator: value => value.length >= 4 && value.length < 2000,
            message: '내용은 4글자 이상, 2000글자 미만이어야 합니다.'
        }
    };

    /**
     * categoryId와 verifyPassword 필드를 필터링한 객체를 반환하는 함수
     * @returns 필드를 필터링한 객체
     */
    const filteredFields = () => {
        const filtered = { ...fieldValidators };
        delete filtered.categoryId;
        delete filtered.verifyPassword;
        return filtered;
    }

    return {
        fieldValidators,
        filteredFields
    };
}