package js.StreamingMusic.security;

import js.StreamingMusic.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //파라미터로 넘어온 authentication에는 로그인할 때 넘긴 사용자의 id, pw가 저장돼있음

        String username = authentication.getName();
        String password = (String)authentication.getCredentials();

        MemberContext memberContext = (MemberContext)userDetailsService.loadUserByUsername(username);  //로그인할 때 pw가 db에 저장된 pw와 같은지 검증을 하기위해 db에서 엔티티를 끄내오는것이다

        if(!passwordEncoder.matches(password, memberContext.getMember().getPassword())) {  //pw검증
            throw new BadCredentialsException("Invalid password");  //pw틀리면 예외터뜨림 -> 인증실패
        }


        ///// id, pw 검증 성공완료/////

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberContext.getMember(), null, memberContext.getAuthorities());
        // UsernamePasswordAuthenticationToken 클래스를 들여다 보면 생성자가 두개 있는데 첫번째 생성자는 처음에 생기는 authentication인증객체, 두번쨰는 최종 인증 객체 우리는 지금 최종 인증개체니깐 두번째 생성자에 맞는 파라미터를 넘긴다다
        //ppt #03.Authentication에서 파라미터 정보를 확인 할 수 있다. 비번을 null로 넘긴이유는 보안상 이유다.

        return authenticationToken;  //AuthenticationManager에게 최종인증 객체를 넘긴다. (authenticate함수를 AuthenticationManager가 호출한다.)
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
