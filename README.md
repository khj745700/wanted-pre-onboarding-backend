# wanted-pre-onboarding-backend


## 1. 지원자의 성명
김현진

<br>
<br>
<br>

## 2. 애플리케이션의 실행 방법 (엔드포인트 호출 방법 포함)
- Docker Compose가 구현되어있습니다.
  프로젝트 루트 디렉토리에 compose-docker up 을 쳐주시면 애플리케이션이 실행됩니다.
  백그라운드에서 실행을 원하시면 compose-docker up -d 명령어를 사용해주시면 애플리케이션이 백그라운드에서 동작됩니다.


- 엔드포인트 호출은 저는 포스트맨으로 진행했습니다.

  
  회원가입 : http://localhost:8080/signup (POST) <br>
  로그인 : http://localhost:8080/login (POST) <br>
  게시글 작성 : http://localhost:8080/board (POST) <br>
  게시글 수정 : http://localhost:8080/board/{boardId} (PUT) <br>
  게시글 삭제 : http://localhost:8080/board/{boardId} (DELETE) <br>
  게시글 단일 조회 : http://localhost:8080/board/{boardId} (GET) <br>
  게시글 페이징 조회 : http://localhost:8080/board?page={숫자}&size=20 (GET), size 파라미터는 안넣어도 됩니다.

상세 방법은 데모 영상 링크로 확인해주시면 되고 JSON Body는 API 명세를 확인해 주세요.
<br>
<br>
EC2에 Docker compose를 올리려 했으나 t3.micro 상에서는 시스템이 다운되는 현상이 발생해,

Code Deploy를 활용해 EC2로 배포, Mysql은 RDS로 사용해야 하는 문제가 있어 이는 CI/CD를 구축 중에 있습니다.


<br>
<br>
<br>

## 3. 데이터베이스 테이블 구조

![스크린샷 2023-08-06 오후 10 22 21](https://github.com/khj745700/wanted-pre-onboarding-backend/assets/68643347/da11def2-7801-4797-a538-09daaba81852)


- password는 BcryptEncoder로 암호화 되서 저장되도록 구현했습니다.

<br>
<br>
<br>

## 4. 구현한 API의 동작을 촬영한 데모 영상 링크

[다운로드 링크](https://drive.google.com/file/d/1mDOy4A2eIoeoB-6HGNO7BVdc4O-M3v7J/view?usp=sharing)
<br>
<br>
<br>


## 5. 구현 방법 및 이유에 대한 간략한 설명

### CustomException 및 GlobalException
![스크린샷 2023-08-06 오후 11 17 12](https://github.com/khj745700/wanted-pre-onboarding-backend/assets/68643347/e2ce7e1e-5897-48fd-a2ca-37c9dfc532e3)

<img width="1175" alt="스크린샷 2023-08-06 오후 11 18 16" src="https://github.com/khj745700/wanted-pre-onboarding-backend/assets/68643347/33f2525b-a373-4f38-9e0c-5d7b78ee9f57">

서버를 개발하다 보면, 많은 Runtime Error와 마주치게 됩니다. 그럴때마다 Exception을 하나씩 정의해서 핸들링 하기 매우 번거로운 상황에 발생합니다.<br>
그렇기 때문에, RestControllerAdvice를 활용해서 ExceptionHandler의 CustomException을 활용해서 적용시키면 추상적으로 동작하므로 유지보수적으로 용이합니다.

<br>
<br>

### DTO Valiate
<img width="629" alt="스크린샷 2023-08-06 오후 11 20 47" src="https://github.com/khj745700/wanted-pre-onboarding-backend/assets/68643347/d1c2f9e4-0be0-4eab-a601-5d6819749f23">

대표적으롤 회원가입 DTO를 가져왔습니다. 회원가입 시에, NotNull 체킹과 정규식을 활용해서 공백 제거 등 controller 단에 접근하기 전 ArgumentResolver가 그전에 throw 해주기 위해 적용했습니다.
그럼 위에 GlobalException에 선언해 놓은 MethodArgumentNotValidException Handler가 동작하여 리턴해주게 됩니다.

### BcryptEncode
<img width="601" alt="스크린샷 2023-08-06 오후 11 26 13" src="https://github.com/khj745700/wanted-pre-onboarding-backend/assets/68643347/1894c037-969e-4fb0-9648-f9109456dd6f">
위 DTO Valdiate 에서 적용된 to Entity() 단에 보시면 ArgumentResolver가 정상적으로 동작했다면 toEntity()는 Service 단에서 만 돌기 때문에 bcrypt 암호화 해서 salt를 붙여서 데이터베이스에 저장됨을 확인할 수 있습니다.

### 로그인 및 수정 권한 체킹
AOP 및 interface static method를 활용한 처리를 하였습니다.<br>
<img width="280" alt="스크린샷 2023-08-06 오후 11 27 23" src="https://github.com/khj745700/wanted-pre-onboarding-backend/assets/68643347/8fb23e46-ccd6-49ad-993e-b63daf6466a7"><br>
- 먼저 LoginCheck어노테이션을 위와 같이 생성합니다.<br>

<img width="662" alt="스크린샷 2023-08-06 오후 11 27 50" src="https://github.com/khj745700/wanted-pre-onboarding-backend/assets/68643347/7c844bb7-7995-484a-a3e1-40d8e287aaff"><br>
- 그리고 aop를 활용해 로그인이 되었는지(Http Header에 Authorization의 JWT access 토큰이 있어야 함), SecurityContextHolder에 선언되어있는 Principle을 가져오는 Contextholder클래스를 활용해 현재 세션을 가져옵니다.
- 만약 null이라면 로그인 하지 않은 경우이므로(access Token이 누락된 경우) 에러를 던집니다.<br>

<img width="571" alt="스크린샷 2023-08-06 오후 11 28 14" src="https://github.com/khj745700/wanted-pre-onboarding-backend/assets/68643347/a93de1f7-ade4-4249-adbf-abdfc683127d"><br>
- 그리고 Board Entity에 EditAuthenticationCheck를 상속 받습니다.<br>

<img width="403" alt="스크린샷 2023-08-06 오후 11 28 46" src="https://github.com/khj745700/wanted-pre-onboarding-backend/assets/68643347/b9aded13-9134-4a2d-9280-7dc4cca9075b"><br>
- 그러면 static 메서드로 인증 체크하는 기능이 있음을 확인할 수 있습니다.<br>

<img width="649" alt="스크린샷 2023-08-06 오후 11 36 25" src="https://github.com/khj745700/wanted-pre-onboarding-backend/assets/68643347/3571a186-1a8e-4b4f-856c-b540cb0c136e"><br>
- 그리고 Member의 HashCode와 equals를 오버라이딩 합니다.<br>
- 그 이유는, Member는 SecurityContextHolder에서 온 객체이므로 영속성 관리가 되어 있지 않는 객체입니다. 따라서 equals나 hashcode가 동작하지 않을 가능성이 있기 때문에, 오버라이딩 해주어 차 후 기능에 문제가 없이 동작하도록 하였습니다.<br>

<img width="490" alt="스크린샷 2023-08-06 오후 11 29 11" src="https://github.com/khj745700/wanted-pre-onboarding-backend/assets/68643347/e24cdce5-5e97-4f63-a1dc-07b42fe76d0b"><br>
- 그러면 Service 단에서 수정및 삭제 권한이 필요한 객체들은 현재 세션을 가져와서 접근하면 되므로 사용이 편리하게 됩니다.


## 6. API 명세(request/response 포함)

<details>
<summary><h3>회원가입</h3></summary>
<div markdown="1">
  
|엔드포인트|메서드명|
|---|---|
|/signup|post|

### request
```json
{
    "username": "@",
    "password": "12345678"
}
```


### response
성공 시
202 Accepted

실패 시
- 이메일 검증 실패
400 Bad Request
```json
{
    "errorMessage": "값이 유효하지 않습니다. (@가 포함되어야 합니다.)",
    "httpStatus": "BAD_REQUEST"
}
```

- 비밀번호 검증 실패
400 Bad Request
```json
{
    "errorMessage": "값이 유효하지 않습니다. (8글자 이상이어야 합니다.)",
    "httpStatus": "BAD_REQUEST"
}
```

- 중복 회원 존재
400 Bad Request
```json

```

<br>
<br>

</details>
</div>



<details>
<summary><h3>로그인</h3></summary>
<div markdown="1">

|엔드포인트|메서드명|
|---|---|
|/login|post|

### request
```json
{
    "username": "@",
    "password": "12345678"
}
```


### response
성공 시
200 OK

실패 시
```c
"Invalid Username or Password"
```

<br>
<br>

</details>
</div>


<details>
<summary><h3>게시글 작성</h3></summary>
<div markdown="1">

|엔드포인트|메서드명|
|---|---|
|/board|post|

### request
```json
{
    "title":"hello",
    "description":"test"
}
```


### response
성공 시
200 OK

실패 시
- 로그인 되어 있지 않음
401 Unauthorized
```json
{
    "errorMessage": "로그인 후 이용해주세요",
    "httpStatus": "UNAUTHORIZED"
}
```

- 제목이 작성되어 있지 않음.
400 BadRequest
```json
{
    "errorMessage": "제목을 채워야 합니다.",
    "httpStatus": "BAD_REQUEST"
}
```

- 내용이 작성되어 있지 않음.
400 BadRequest
```json
{
    "errorMessage": "내용을 채워야 합니다.",
    "httpStatus": "BAD_REQUEST"
}
```

<br>
<br>

</details>
</div>


<details>
<summary><h3>게시글 수정</h3></summary>
<div markdown="1">

|엔드포인트|메서드명|
|---|---|
|/board/{id}|put|

### request
```json
{
    "title":"hello",
    "description":"test"
}
```


### response
성공 시
200 OK

실패 시
- 로그인 되어 있지 않음
401 Unauthorized
```json
{
    "errorMessage": "로그인 후 이용해주세요",
    "httpStatus": "UNAUTHORIZED"
}
```

- 수정 권한이 없음.
- 403 FORBIDDEN
```json
{
    "errorMessage": "수정할 수 없습니다. 잘못된 접근입니다.",
    "httpStatus": "FORBIDDEN"
}
```

- 제목이 작성되어 있지 않음.
400 BadRequest
```json
{
    "errorMessage": "제목을 채워야 합니다.",
    "httpStatus": "BAD_REQUEST"
}
```

- 내용이 작성되어 있지 않음.
400 BadRequest
```json
{
    "errorMessage": "내용을 채워야 합니다.",
    "httpStatus": "BAD_REQUEST"
}
```

<br>
<br>

</details>
</div>


<details>
<summary><h3>게시글 삭제</h3></summary>
<div markdown="1">

|엔드포인트|메서드명|
|---|---|
|/board/{id}|delete|

성공 시
202 Accepted

실패 시
- 로그인 되어 있지 않음
401 Unauthorized
```json
{
    "errorMessage": "로그인 후 이용해주세요",
    "httpStatus": "UNAUTHORIZED"
}
```

- 수정 권한이 없음.
- 403 FORBIDDEN
```json
{
    "errorMessage": "수정할 수 없습니다. 잘못된 접근입니다.",
    "httpStatus": "FORBIDDEN"
}
```


<br>
<br>

</details>
</div>


<details>
<summary><h3>게시글 단일 조회</h3></summary>
<div markdown="1">

|엔드포인트|메서드명|
|---|---|
|/board/{id}|get|

### request



### response
성공 시
```json
{
  "title":"안녕하세요",
  "description": "신입입니다!",
  "username": "@"
}
```

실패 시
404 NOT FOUND
```json
{
  "errorMessage": "해당 자원을 찾을 수 없습니다.",
  "httpStatus": "NOT_FOUND"
}
```


<br>
<br>

</details>
</div>

<details>
<summary><h3>게시글 페이지 조회</h3></summary>
<div markdown="1">

|엔드포인트|메서드명|
|---|---|
|/board?page={pageNum}|get|

### request



### response
성공 시
```json
{
    "content": [
        {
            "boardId": 1,
            "title": "hello",
            "description": "test",
            "username": "@"
        },
        {
            "boardId": 2,
            "title": "hello",
            "description": "test",
            "username": "@"
        },
        {
            "boardId": 3,
            "title": "hello",
            "description": "test",
            "username": "@"
        },
        {
            "boardId": 4,
            "title": "hello",
            "description": "test",
            "username": "@"
        },
        {
            "boardId": 5,
            "title": "hello",
            "description": "test",
            "username": "@"
        },
        {
            "boardId": 6,
            "title": "hello",
            "description": "test",
            "username": "@"
        },
        {
            "boardId": 7,
            "title": "hello",
            "description": "test",
            "username": "@"
        },
        {
            "boardId": 8,
            "title": "hello",
            "description": "test",
            "username": "@"
        },
        {
            "boardId": 9,
            "title": "hello",
            "description": "test",
            "username": "@"
        },
        {
            "boardId": 10,
            "title": "hello",
            "description": "test",
            "username": "@"
        },
        {
            "boardId": 11,
            "title": "hello",
            "description": "test",
            "username": "@"
        },
        {
            "boardId": 12,
            "title": "hello",
            "description": "test",
            "username": "@"
        },
        {
            "boardId": 13,
            "title": "hello",
            "description": "test",
            "username": "@"
        },
        {
            "boardId": 14,
            "title": "hello",
            "description": "test",
            "username": "@"
        },
        {
            "boardId": 15,
            "title": "hello",
            "description": "test",
            "username": "@"
        },
        {
            "boardId": 16,
            "title": "hello",
            "description": "test",
            "username": "@"
        }
    ],
    "pageable": {
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 20,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 16,
    "first": true,
    "size": 20,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "numberOfElements": 16,
    "empty": false
}
```

</details>
</div>
