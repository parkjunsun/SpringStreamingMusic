# StreamingMusic

Spring Boot 기반 웹 음악 스트리밍 프로젝트입니다.
Genie 차트/검색 크롤링 데이터와 YouTube API를 결합해 곡 탐색, 재생, 플레이리스트 저장 기능을 제공합니다.

## 프로젝트 소개
- 목적: 웹에서 음악을 검색하고, 개인 플레이리스트로 관리
- 인증: 일반 로그인 + OAuth2 로그인(Google/Facebook/Naver/Kakao)
- 주요 화면: 홈, 검색, 차트, 플레이리스트, 마이페이지, 관리자

## 핵심 기능
- 음악 탐색
  - Genie 기반 검색/차트/신곡 조회
  - 앨범/곡 상세 페이지 제공
- 플레이리스트
  - 곡 추가/삭제
  - 장르별 그룹 조회
  - YouTube `videoId` 직접 추가
- 사용자 기능
  - 마이페이지(재생 기록, 게시글 목록, 회원정보 수정)
- 커뮤니티/관리
  - 게시글(댓글) 작성/수정/삭제
  - 관리자 페이지(회원 조회, 권한 수정, 게시글/회원 삭제)

## 기술 스택
- Backend: Java 11, Spring Boot 2.4.2, Spring MVC, Spring Security, Spring Data JPA
- View: Thymeleaf
- DB: PostgreSQL
- Crawling/API: Jsoup, YouTube Data API v3
- Build: Gradle Wrapper

## 프로젝트 구조
```text
src/main/java/js/StreamingMusic
|- controller/
|- service/
|- repository/
|- domain/
|  |- entity/
|  |- dto/
|- security/
|- oauth/

src/main/resources
|- templates/
|- static/
|- application.properties
```

## 빠른 시작
### 1) 요구 사항
- JDK 11
- PostgreSQL
- YouTube API 키
- OAuth2 클라이언트 키(google/facebook/naver/kakao)

### 2) 실행
```powershell
.\gradlew.bat bootRun
```

### 3) 테스트
```powershell
.\gradlew.bat test
```

## 설정
현재 프로젝트는 `src/main/resources/application.properties`를 사용합니다.
GitHub 공개 저장소에서는 민감정보(DB 비밀번호, OAuth Secret, API Key)를 직접 커밋하지 않는 것을 권장합니다.

권장 예시:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}

youtube.data.apikey=${YOUTUBE_DATA_APIKEY}

spring.mail.username=${SPRING_MAIL_USERNAME}
spring.mail.password=${SPRING_MAIL_PASSWORD}

spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
```

## 주요 엔드포인트
- `/` 홈
- `/login` 로그인
- `/register` 회원가입
- `/search` 검색
- `/chart/top200/{pgNum}` 차트
- `/chart/new/{pgNum}` 신곡
- `/playlist` 플레이리스트
- `/user/{username}` 마이페이지
- `/admin` 관리자

## 문서
- 추천 시스템 계획: `docs/recommendation-system-plan.md`

## 로드맵
- 보안 설정 정리(CSRF/redirect 처리)
- DTO/예외 처리 리팩토링
- 테스트 커버리지 확대
- 추천 시스템 단계적 도입
