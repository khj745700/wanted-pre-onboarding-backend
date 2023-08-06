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

![스크린샷 2023-08-06 오후 4 25 32](https://github.com/khj745700/wanted-pre-onboarding-backend/assets/68643347/1a659b05-2630-4426-9399-33eddb745833)
- password는 BcryptEncoder로 암호화 되서 저장되도록 구현했습니다.

<br>
<br>
<br>

## 4. 구현한 API의 동작을 촬영한 데모 영상 링크


<br>
<br>
<br>

## 5. API 명세(request/response 포함)


