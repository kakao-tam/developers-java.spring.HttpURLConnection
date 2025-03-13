# Spring Boot Kakao API Integration

Spring Boot를 사용하여 카카오 API를 연동하는 예제 프로젝트입니다. HttpURLConnection을 사용하여 카카오 로그인과 메시지 전송 기능을 구현했습니다.

## 주요 기능

- 카카오 로그인 연동
- 카카오 메시지 전송
- HttpURLConnection을 사용한 HTTP 통신

## 기술 스택

- Java 21
- Spring Boot 3.2.3
- Gradle 8.5
- Lombok

## 시작하기

### 사전 요구사항

- JDK 21 이상
- 카카오 개발자 계정 및 애플리케이션 등록
- 카카오 API 키 (Client ID, Client Secret)

### 환경 설정

1. 카카오 개발자 콘솔에서 애플리케이션을 등록하고 다음 정보를 설정합니다:
   - Redirect URI: `http://localhost:8080/api/kakao/redirect`
   - 카카오 로그인 활성화
   - 카카오 메시지 전송 권한 설정

2. `application.yml` 파일에 카카오 API 설정을 추가합니다:
   ```yaml
   kakao:
     client-id: ${KAKAO_CLIENT_ID:your_client_id_here}
     client-secret: ${KAKAO_CLIENT_SECRET:your_client_secret_here}
     redirect-uri: http://localhost:8080/api/kakao/redirect
   ```

### 실행 방법

1. 프로젝트 클론
   ```bash
   git clone https://github.com/kakao-tam/developers-java.spring.HttpURLConnection.git
   cd developers-java.spring.HttpURLConnection
   ```

2. 환경 변수 설정
   ```bash
   export KAKAO_CLIENT_ID=your_client_id_here
   export KAKAO_CLIENT_SECRET=your_client_secret_here
   ```

3. 애플리케이션 실행
   ```bash
   ./gradlew bootRun
   ```

4. 브라우저에서 접속
   - http://localhost:8080 으로 접속하여 카카오 로그인 테스트

## API 엔드포인트

- `GET /api/kakao/login`: 카카오 로그인 페이지로 리다이렉트
- `GET /api/kakao/redirect`: 카카오 로그인 콜백 처리
- `POST /api/kakao/message`: 카카오 메시지 전송

## 프로젝트 구조

```
src/main/java/com/example/demo/
├── DemoApplication.java
├── controller/
│   └── KakaoController.java
├── service/
│   └── KakaoApiService.java
└── dto/
    ├── KakaoTokenResponse.java
    └── KakaoMessageRequest.java
```

## 스크린샷

<img width="817" alt="image" src="https://github.com/user-attachments/assets/becf2d5d-8ba9-454d-977f-07cf823e0152" />

