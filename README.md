# 백엔드 프로젝트입니다.

## 시스템 구조도
![마이크로서비스 설계](https://user-images.githubusercontent.com/55542546/167294172-c8cd06c6-9f0a-433a-a550-97426e0f9535.jpg)

## rest api
### user 마이크로서비스(/user-service)

| method  | uri  | description  | return | success | fail |
|:----------|:----------|:----------|:-------|:-----------|:-----------|
| post    | /register    | 회원가입   | String | CREATED |  |
| post    | /login    | 로그인    | Boolean | OK | |
| post    | /logout    | 로그아웃    | Boolean | OK | NOT_FOUND |
| put    | /update    | 회원정보수정    | Boolean | CREATED | NOT_FOUND  |
| delete | /withdraw    | 회원탈퇴    | Boolean | OK | NOT_FOUND |

### board 마이크로서비스(/board-service)

| method  | uri  | description  | return | success | fail |
|:----------|:----------|:----------|:--------|:------------|:-----------|
| post    | /write    | 글 작성   | Long | CREATED | |
| put    | /{id}    | 글 수정    | Boolean | CREATED | NOT_FOUND |
| delete | /{id}    | 글 삭제    | Boolean | OK | NOT_FOUND |
| get | /{id} | 글 조회 | Board | OK | NOT_FOUND |
| get | / | 글 목록 조회 | List<Board> | OK | NOT_FOUND |  
