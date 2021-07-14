package js.StreamingMusic.oauth;

import js.StreamingMusic.domain.entity.Member;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        System.out.println(userRequest.getClientRegistration().getRegistrationId()); //registrationId로 어떤 OAuth로 로그인했는지 확인 가능
//        System.out.println(userRequest.getAccessToken());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글 로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(OAuth-Client라이브러리가 받음) -> AccessToken요청
        // userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필을 받아준다.
//        System.out.println(oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();// google
        String providerId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String username = provider + "_" + providerId; //google_2039281390
        String password = UUID.randomUUID().toString();
        String role = "ROLE_USER";

        List<Member> memberList = memberService.findByUsername(username);
        if (memberList.size() == 0) {
            Member member = new Member();
            member.setUsername(username);
            member.setPassword(password);
            member.setEmail(email);
            member.setRole(role);
            member.setProvider(provider);
            member.setProviderId(providerId);
            member.setJoinDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            memberService.join(member);
            return new MemberContext(member, oAuth2User.getAttributes());
        } else {
            Member member = memberList.get(0);
            return new MemberContext(member, oAuth2User.getAttributes());
        }

    }
}
