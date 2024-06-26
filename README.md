# 웹개발 취준생을 위한 포트폴리오 스터디

<a href="#"><img src="https://img.shields.io/github/last-commit/yu-jin-song/eb-study.svg?style=flat" /></a>
<a href="#"><img src="https://img.shields.io/github/languages/top/yu-jin-song/eb-study.svg?colorB=yellow&style=flat" /></a>

게시판 구현 프로젝트
- 기간 : 2024.01.20 ~ 2024.03.10 (약 8주)
- 인원 : 1명(개인)

<br><br>

## 💡 단계별 구현
> 각 단계별 소스코드는 해당 디렉토리로 이동하여 확인하실 수 있습니다. 링크를 누르면 해당 디렉토리로 이동합니다.
<table>
  <tr>
    <th></th>
    <th>구현</th>
    <th>설명</th>
    <th>개발도구</th>
  </tr>
  <tr>
    <td>1단계</td>
    <td><a href="https://github.com/yu-jin-song/eb-study/tree/master/week-1-jsp">JSP 게시판</a></td>
    <td>MVC 패턴 Model 1 적용</td>
    <td>Java, JSP, MariaDB, JDBC</td>
  </tr>
  <tr>
    <td>2단계</td>
    <td><a href="https://github.com/yu-jin-song/eb-study/tree/master/week-2-servlet">Servlet 게시판</a></td>
    <td>MVC 패턴 Model 2 적용</td>
    <td>Java, Servlet, JSP, MariaDB, JDBC</td>
  </tr>
  <tr>
    <td>3단계</td>
    <td><a href="https://github.com/yu-jin-song/eb-study/tree/master/week-3-springboot">Spring Boot 게시판</a></td>
    <td>Framework 사용</td>
    <td>Java, Spring Boot, JavaScript,<br>Tyhmeleaf, MariaDB, MyBatis</td>
  </tr>
  <tr>
    <td>4단계</td>
    <td><a href="https://github.com/yu-jin-song/eb-study/tree/master/week-4-restapi">Spring Boot API - Vue.js 게시판</a></td>
    <td>Restful API를 통한 Server/Frontend 분리</td>
    <td>Java, Spring Boot, Vue.js,<br>JavaScript, MariaDB, MyBatis</td>
  </tr>
</table>

<br><br>

## ⚙️ 사전 준비
### ✅ JDK 설치(버전 17 권장)
https://velog.io/@yu-jin-song/OpenJDK-설치-가이드

### ✅ Docker Desktop 설치
https://www.docker.com/products/docker-desktop/

### ✅ Apache Tomcat 설치
https://tomcat.apache.org/download-90.cgi

### ✅ Docker Compose 실행 - MySql
``` 
cd help
docker-compose up -d
```

<br><br>

## 🔎 설계
![board_erd](https://github.com/yu-jin-song/eb-study/assets/74666378/e12f9399-c2a5-4f71-9b80-df6518cc99a9)

<br><br>

## 📖 구현 기능
### ✅ 게시글 목록
- **게시글 목록**을 표시하는 페이지입니다. 기본적으로 오늘 날짜로부터 1년 전까지의 게시글 목록이 최신순으로 출력됩니다.
![board_list](https://github.com/yu-jin-song/eb-study/assets/74666378/f297238f-649e-4418-a70a-98dee35449f9)

- **검색 조건**(날짜, 카테고리, 검색어)을 적용할 수 있습니다. 검색어의 경우, 해당 검색어가 들어간 제목, 작성자, 게시글 내용을 검색할 수 있습니다.
![board_list_search](https://github.com/yu-jin-song/eb-study/assets/74666378/c268b249-b202-4ced-9056-27aeaf7b8c64)

- 페이지 이동 시 적용된 검색 조건이 유지됩니다.
![board_list_search_move_page](https://github.com/yu-jin-song/eb-study/assets/74666378/5fb7ddb1-c616-4e61-ba4b-fcd993e3f119)

<br><br>

### ✅ 게시글 작성
- **등록** 버튼을 누르면 게시글을 **작성**할 수 있습니다. **파일**을 **첨부**할 수 있으며, 최대 **3개**까지 가능합니다.
![write_post](https://github.com/yu-jin-song/eb-study/assets/74666378/087e576e-e2a3-4636-96ba-49456d5f6f46)

<br><br>

### ✅ 게시글 상세
- 제목을 누르면 해당 **게시글 상세** 페이지로 이동합니다.
![into_board_detail](https://github.com/yu-jin-song/eb-study/assets/74666378/5272e318-fe20-4db8-992f-0de7850a981c)

- 첨부파일을 다운로드 할 수 있습니다. 해당 폴더로 이동하면 다운로드한 파일을 확인할 수 있습니다.
![download_file](https://github.com/yu-jin-song/eb-study/assets/74666378/70d3e0a3-132a-474e-bf70-bf429ea24b05)
![download_check](https://github.com/yu-jin-song/eb-study/assets/74666378/f5504c43-a6cd-4133-9774-593e19efa0cb)

- **댓글**을 작성할 수 있습니다.
![board_detail_comment](https://github.com/yu-jin-song/eb-study/assets/74666378/b7135450-17ad-49bb-abc0-1f362a8260a0)

- **목록** 버튼을 누르면 게시글 목록 조회 페이지로 이동합니다.
![go_to_board_list](https://github.com/yu-jin-song/eb-study/assets/74666378/81bb6b49-09f8-4953-a9cd-fb9eedd4a219)

<br><br>

## ✅ 게시글 수정
- **수정** 버튼을 누르면  해당 게시글의 비밀번호와 입력한 **비밀번호**가 **일치**하는 지 확인하는 과정을 거친 후, 수정 페이지로 이동합니다. 첨부된 **파일**을 **삭제**하거나 **추가**할 수 있습니다.
![update_post](https://github.com/yu-jin-song/eb-study/assets/74666378/9ea6d063-44ef-4115-9392-0bb170d026b9)

<br><br>

## ✅ 게시글 삭제
- **삭제** 버튼을 누르면 해당 게시글의 비밀번호와 입력한 **비밀번호**가 **일치**하는 지 확인하는 과정을 거친 후, 해당 게시글을 삭제합니다.
![delete_post](https://github.com/yu-jin-song/eb-study/assets/74666378/5c65358d-ea14-488b-9f95-88f27044cd7f)

