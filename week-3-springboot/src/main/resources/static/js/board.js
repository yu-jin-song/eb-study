// TODO : 분리중
// <script>
//     // 파일 삭제 로직
//     function deleteFile(formName, fileId) {
//         const fileNameInput = document.getElementById(formName + "FileName" + fileId);
//         if (fileNameInput) {
//             fileNameInput.parentNode.removeChild(fileNameInput);
//         }
//
//         const fileInputContainer = document.getElementById(formName + "File" + fileId);
//         if (fileInputContainer) {
//             fileInputContainer.innerHTML = '<input type="file" name="uploadFile"/>';
//         }
//     }
//
//     // 폼 로드 시 실행
//     document.addEventListener("DOMContentLoaded", function() {
//     // 작성 폼 및 수정 폼 설정
//     const forms = {
//     postForm: {
//     writer: {
//     maxLength: 4,
//     validator: value => value.length >= 3 && value.length <= 4,
//     message: "작성자는 3글자 이상, 4글자 이하이어야 합니다."
// },
//     password: {
//     maxLength: 16,
//     validator: value => /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/.test(value),
//     message: "비밀번호는 8글자 이상이며, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 포함해야 합니다."
// },
//     title: {
//     maxLength: 100,
//     validator: value => value.length >= 1 && value.length <= 100,
//     message: "제목은 1글자 이상, 100글자 이하이어야 합니다."
// },
//     content: {
//     maxLength: 2000,
//     validator: value => value.length >= 1 && value.length <= 2000,
//     message: "내용은 1글자 이상, 2000글자 이하이어야 합니다."
// }
// },
//     modifyForm: {
//     // 수정 폼의 경우, 작성자 필드가 없을 수 있으므로 작성자 필드 관련 설정은 제외
//     password: {
//     maxLength: 16,
//     validator: value => /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/.test(value),
//     message: "비밀번호는 8글자 이상이며, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자를 포함해야 합니다."
// },
//     title: {
//     maxLength: 100,
//     validator: value => value.length >= 1 && value.length <= 100,
//     message: "제목은 1글자 이상, 100글자 이하이어야 합니다."
// },
//     content: {
//     maxLength: 2000,
//     validator: value => value.length >= 1 && value.length <= 2000,
//     message: "내용은 1글자 이상, 2000글자 이하이어야 합니다."
// }
// }
// };
//
//     // 각 폼에 대한 유효성 검사 로직
//     Object.keys(forms).forEach(formName => {
//     const form = document.forms[formName];
//     const fields = forms[formName];
//
//     Object.keys(fields).forEach(field => {
//     const input = form[field];
//     if(input) { // 필드가 실제로 폼에 존재하는 경우에만 이벤트 리스너 추가
//     input.addEventListener("blur", function() {
//     validateField(form, field, fields[field]);
// });
// }
// });
//
//     const saveButton = document.getElementById(formName + "SaveButton");
//     if(saveButton) { // 저장 버튼이 실제로 존재하는 경우에만 클릭 이벤트 리스너 추가
//     saveButton.addEventListener("click", function(event) {
//     event.preventDefault();
//     if (validateAllFields(form, fields)) {
//     form.submit();
// } else {
//     alert("양식을 올바르게 채워주세요.");
// }
// });
// }
// });
//
//     function validateField(form, field, {validator, message}) {
//     const input = form[field];
//     const isValid = validator(input.value);
//     if (!isValid) {
//     alert(message);
//     input.focus();
// }
// }
//
//     function validateAllFields(form, fields) {
//     return Object.keys(fields).every(field => {
//     const {validator, message} = fields[field];
//     const input = form[field];
//     if(input) { // 필드가 실제로 폼에 존재하는 경우에만 유효성 검사 진행
//     const isValid = validator(input.value);
//     if (!isValid) {
//     alert(message);
//     input.focus();
//     return false;
// }
// }
//     return true;
// });
// }
// });
// </script>