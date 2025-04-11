# Kakao REST API Java Spring HttpURLConnection 예제

이 프로젝트는 Kakao REST API를 Java Spring HttpURLConnection로 구현한 예제입니다.

## 주요 기능

- 카카오 로그인
- 사용자 정보 가져오기
- 친구 목록 가져오기
- 나에게 메시지 발송
- 친구에게 메시지 발송
- 로그아웃
- 연결 끊기

## 프로젝트 구조
```
.
│
├── /src/resources/static/index.html # 메인 HTML 파일
└── /src/resources/static/style.css # 스타일시트
├── src/main/java/com/example/demo/controller/KakaoController.java # 컨트롤러 파일
├── src/main/java/com/example/demo/service/KakaoApiService.java # 서비스 파일
├── src/main/resources/application.yml # 설정 파일
└── README.md # 프로젝트 설명 파일
```

## 설치 방법

1. 프로젝트 클론
```bash
git clone [repository-url]
cd [project-directory]
```

2. 의존성 설치
```bash
gradle wrapper
./gradlew build       # macOS/Linux
gradlew.bat build     # Windows
```

3. 카카오 개발자 설정
- [Kakao Developers](https://developers.kakao.com)에서 애플리케이션 생성
- `application.properties`의 `client-id`와 `client-secret`을 발급받은 값으로 변경
- 카카오 개발자 콘솔의 "카카오 로그인 > 플랫폼 > Web 플랫폼"에서 사이트 도메인을 등록합니다.
- 카카오 개발자 콘솔의 "카카오 로그인 > 카카오 로그인 활성화"를 ON으로 설정합니다.
- Redirect URI 설정: `http://localhost:8080/api/kakao/redirect`

4. 서버 실행
```bash
./gradlew bootRun
```

## 사용 방법

1. 브라우저에서 `http://localhost:8080` 접속
2. 카카오 로그인 버튼 클릭
3. 각 기능 버튼을 통해 API 테스트

## 주의사항

- 카카오 로그인 Redirect URI가 정확히 설정되어 있어야 합니다.
- 친구 목록 조회와 메시지 발송을 위해서는 추가 동의가 필요합니다.

## 의존성

- Spring Boot: 2.7.0
- Spring Web
- Spring Security
- Jackson

## 스크린샷
![image](https://github.com/user-attachments/assets/a64d2a83-c036-4cb2-88e5-07bba3890ec3)

