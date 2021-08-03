## Music Streaming Web Service
🎵음원이나 youtube 라이브 음원을 골라 나만의 플레이리스트에 넣어 들을 수 있는 뮤직 스트리밍 웹서버입니다🎵

***

## 🚀개발개요
* 음악 스트리밍 가격이 너무 비싸지고 있다. 모바일 스트리밍 요금만 내고 있다면 데스크탑 스트리밍 서비스를 이용하려면 추가요금이 발생한다. 금액적인 부담을 해소 하고 싶었다. youtube로는 노래를 무료로 들을 수 있는 점에 착안하여 서비스를 만들어 보았다.

* 대부분의 스트리밍 서비스는 노래를 플레이리스트에 담은 후에는 그룹화를 할 수가 없다. 미리 그룹을 만들어 놓은 후에 노래를 플레이리스트에 담을 때 그룹화를 해야한다. 기존 서비스의 이러한 부분이 불편함 점으로 다가와 개선된 서비를 만들고자 했다.

* 개인적으로 노래감상하는 것을 좋아한다. 음원도 물론 많이 듣지만 가수들의 라이브 공연 무대를 듣는 것을 좋아한다. 기존 스트리밍 서비스에서는 음원만 있을 뿐 라이브 공연을 들을 수는 없다. 나와 같이 라이브 공연 듣는 것을 좋아하는 사람들을 위해 플레이리스트에 음원 뿐만 아니라 라이브 공연도 넣을 수 있는 서비스를 만들어 보았다.

***

## 📚주요기능
* ### Form 로그인과 Oauth2.0 로그인 통합
<img src = "https://user-images.githubusercontent.com/50009692/127871724-2c5fb78d-bdd3-4b25-ba25-47318514f964.PNG" width="400" height="400">


* ### 메인 사이트 - 노래 검색, 저장, 상세정보, 1회 감상 기능
<img src = "https://user-images.githubusercontent.com/50009692/127873089-e20e6db3-92b4-4201-b5fd-b1c4d50e0a18.PNG"><br>


* ### 웹플레이어 - 반복재생, 셔플, 재생/정지, 다음곡/다시듣기, 재생목록에서 제거, 장르별그룹화
<img src = "https://user-images.githubusercontent.com/50009692/127874552-66beace3-8648-45f7-84f3-58919715e73e.PNG" height="600">

* ### myPage - 가수 및 노래별 감상 횟수 히스토리, 댓글 모음, 회원 정보 수정
<img src = "https://user-images.githubusercontent.com/50009692/127876376-49b18521-67e9-43c9-b53e-5756c24ad179.PNG" height="700">
<img src = "https://user-images.githubusercontent.com/50009692/127876408-840c143f-24ac-4520-ab09-deb4ca75d4b6.PNG" height="700">

***

## ⚒기술스택

***

## ⚙환경설정 (application.properties)
```application.properties
#db 정보

spring.datasource.url=본인 DB URL주소
spring.datasource.username=본인 DB ID
spring.datasource.password=본인 DB PW

#youtube video id 검색 api

youtube.data.apikey = Youtube Data api key

#회원가입 이메일 인증시 필요기능

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=본인 Gmail ID
spring.mail.password=본인 Gmail PW
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

#Google, Facebook, naver, kakao Oauth2.0 인증 api 환경설정

spring.security.oauth2.client.registration.google.client-id=GOOGLE Oauth2.0 api key id
spring.security.oauth2.client.registration.google.client-secret=GOOGLE Oauth2.0 api scret key
spring.security.oauth2.client.registration.google.scope=profile,email,...,etc,...

spring.security.oauth2.client.registration.facebook.client-id=FACEBOOK Oauth2.0 api key id
spring.security.oauth2.client.registration.facebook.client-secret=FACEBOOK Oauth2.0 api scret key
spring.security.oauth2.client.registration.facebook.scope=public_profile,email,....,etc,...

spring.security.oauth2.client.registration.naver.client-id=NAVER Oauth2.0 api key id
spring.security.oauth2.client.registration.naver.client-secret=NAVER Oauth2.0 api secret key
spring.security.oauth2.client.registration.naver.scope=name,email,...,etc,...
spring.security.oauth2.client.registration.naver.client-name=Naver
spring.security.oauth2.client.registration.naver.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.naver.redirect-uri=개발서버url/login/oauth2/code/naver

spring.security.oauth2.client.provider.naver.authorization-uri=https://nid.naver.com/oauth2.0/authorize
spring.security.oauth2.client.provider.naver.token-uri=https://nid.naver.com/oauth2.0/token
spring.security.oauth2.client.provider.naver.user-info-uri=https://openapi.naver.com/v1/nid/me
spring.security.oauth2.client.provider.naver.user-name-attribute=response


spring.security.oauth2.client.registration.kakao.client-id=KAKAO Oauth2.0 api key id
spring.security.oauth2.client.registration.kakao.client-name=Kakao
spring.security.oauth2.client.registration.kakao.scope=profile_nickname,account_email,...,etc,...
spring.security.oauth2.client.registration.kakao.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.kakao.redirect-uri=개발서버url/login/oauth2/code/kakao
spring.security.oauth2.client.registration.kakao.client-authentication-method=POST

spring.security.oauth2.client.provider.kakao.authorization-uri=https://kauth.kakao.com/oauth/authorize
spring.security.oauth2.client.provider.kakao.token-uri=https://kauth.kakao.com/oauth/token
spring.security.oauth2.client.provider.kakao.user-info-uri=https://kapi.kakao.com/v2/user/me
spring.security.oauth2.client.provider.kakao.user-name-attribute=id
```







