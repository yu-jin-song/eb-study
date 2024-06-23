import { ref } from 'vue';

/**
 * 게시물 작성 폼 유효성 검사를 위한 커스텀 훅
 * 
 * @param form - 게시물 작성 폼 데이터
 * @param fields - 각 입력 필드에 대한 유효성 검사 규칙 및 메시지
 * @param onSubmit - 폼 제출 시 호출되는 함수
 * 
 * @returns form, errors, validateField, validateAllFields, submitForm을 포함하는 객체
 */
export default function usePostFormValidation(form, fields, onSubmit) {
  const errors = ref({});

  /**
   * 입력 필드의 유효성을 검사하는 함수
   * 
   * @param {string} field - 검사할 입력 필드의 이름
   */
  const validateField = (field) => {
    const { validator, message, maxLength } = fields[field];
    let value = form.value[field];

    if (value === null) {
      return;
    }

    // 최대 길이를 초과한 경우 값 잘라내기
    if (field !== 'category' && value.length > maxLength) {
      alert(message);
      value = value.substring(0, maxLength);
      form.value[field] = value;
    }

    const isValid = (field === 'verifyPassword') ?
      validator(value, form.value.password) : validator(value);

    if (!isValid) {
      alert(message);
      errors.value[field] = message;
    } else {
      errors.value[field] = '';
    }
  };

  /**
   * 모든 입력 필드의 유효성을 검사하는 함수
   * 
   * @returns {boolean} 모든 입력 필드가 유효한지 여부
   */
  const validateAllFields = () => {
    let allValid = true;

    Object.keys(fields).forEach(field => {
      validateField(field);
      if (errors.value[field]) {
        allValid = false;
      }
    });

    return allValid;
  };

  /**
   * 폼 제출 시 유효성을 검사하고 onSubmit 콜백을 호출하는 함수
   */
  const submitForm = async () => {
    if (validateAllFields()) {
      await onSubmit();
    } else {
      alert('양식을 올바르게 채워주세요.');
    }
  };

  return {
    form,
    errors,
    validateField,
    validateAllFields,
    submitForm
  };
}