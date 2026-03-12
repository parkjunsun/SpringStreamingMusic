# StreamingMusic

Spring Boot + Thymeleaf 기반의 웹 음악 스트리밍 프로젝트입니다.
Genie 차트/검색 크롤링과 YouTube API를 결합해 곡 재생, 플레이리스트 관리, 기록 통계, 소셜 로그인 기능을 제공합니다.

## 프로젝트 개요
- 목적: 데스크톱에서도 부담 없이 음악을 듣고, 나만의 플레이리스트를 유연하게 관리
- 핵심 기능: 자체 계정 로그인 + OAuth2 로그인, 검색/차트 기반 곡 추가, YouTube 직접 추가, 마이페이지 통계
- 프로젝트 타입: 모놀리식 Spring MVC 애플리케이션

## 주요 기능
- 인증/인가
  - Form 로그인
  - OAuth2 로그인(Google, Facebook, Naver, Kakao)
  - Spring Security 기반 권한 제어(USER/ADMIN)
- 음악 탐색
  - Genie 기반 실시간 차트/최신곡/검색 결과 조회
  - 앨범/곡 상세 정보 조회
- 플레이리스트
  - 곡 추가/삭제
  - 장르별 그룹 조회
  - YouTube videoId 직접 추가
  - 마지막 재생 곡 ID 저장
- 사용자 기능
  - 마이페이지(재생 기록, 아티스트별 재생 통계, 작성 게시글)
  - 회원정보 수정
- 커뮤니티/운영
  - 댓글(게시글) 작성/수정/삭제
  - 좋아요
  - 관리자 페이지(회원 조회/권한 변경/회원 삭제/게시글 삭제)

## 기술 스택
- Backend
  - Java 11
  - Spring Boot 2.4.2
  - Spring MVC, Spring Security, Spring Data JPA
  - Thymeleaf
- Data/Infra
  - PostgreSQL
  - JPA(EntityManager 기반 Repository)
- External API / Crawling
  - YouTube Data API v3
  - Jsoup(Genie 페이지 크롤링)
- Build
  - Gradle Wrapper

## 프로젝트 구조
```text
src/main/java/js/StreamingMusic
|- controller/           # MVC 컨트롤러
|- service/              # 비즈니스 로직
|- repository/           # DB 접근(EntityManager)
|- domain/
|  |- entity/            # JPA 엔티티
|  |- dto/               # 화면/쿼리 DTO
|- security/             # Spring Security 설정/핸들러
|- oauth/                # OAuth2 사용자 처리

src/main/resources
|- templates/            # Thymeleaf 템플릿
|- static/               # css/js/image
|- application.properties
```

## 빠른 시작
### 1) 사전 준비
- JDK 11
- PostgreSQL
- YouTube Data API Key
- OAuth2 앱 키(google/facebook/naver/kakao)

### 2) 환경변수 설정 (PowerShell)
아래 값은 코드/설정 파일에 직접 하드코딩하지 않는 것을 권장합니다.

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/springmusicserver"
$env:SPRING_DATASOURCE_USERNAME="postgres"
$env:SPRING_DATASOURCE_PASSWORD="your-db-password"

$env:YOUTUBE_DATA_APIKEY="your-youtube-api-key"

$env:SPRING_MAIL_USERNAME="your-mail@gmail.com"
$env:SPRING_MAIL_PASSWORD="your-mail-app-password"

$env:GOOGLE_CLIENT_ID="your-google-client-id"
$env:GOOGLE_CLIENT_SECRET="your-google-client-secret"
$env:FACEBOOK_CLIENT_ID="your-facebook-client-id"
$env:FACEBOOK_CLIENT_SECRET="your-facebook-client-secret"
$env:NAVER_CLIENT_ID="your-naver-client-id"
$env:NAVER_CLIENT_SECRET="your-naver-client-secret"
$env:KAKAO_CLIENT_ID="your-kakao-client-id"
```

### 3) 실행
```powershell
.\gradlew.bat bootRun
```

기본 포트(현재 설정): `9999`

### 4) 테스트
```powershell
.\gradlew.bat test
```

## 설정 가이드
현재 `src/main/resources/application.properties`는 DB/OAuth/API 관련 키를 직접 입력하는 형태입니다.
운영/공유 환경에서는 아래 패턴으로 바꿔 쓰는 것을 권장합니다.

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

## 주요 엔드포인트 예시
- 홈: `/`
- 로그인: `/login`
- 회원가입: `/register`
- 검색: `/search`
- 플레이리스트: `/playlist`
- 차트: `/chart/top200/{pgNum}`, `/chart/new/{pgNum}`
- 마이페이지: `/user/{username}`
- 관리자: `/admin`
