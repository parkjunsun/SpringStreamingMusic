package js.StreamingMusic.security;

import js.StreamingMusic.domain.entity.Member;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//public class MemberContext extends User{
//
//    private final Member member;
//
//    public MemberContext(Member member, Collection<? extends GrantedAuthority> authorities) {
//        super(member.getUsername(), member.getPassword(), authorities);
//        this.member = member;
//    }
//
//    public Member getMember() {
//        return member;
//    }
//}

@Getter @Setter
public class MemberContext implements UserDetails, OAuth2User {

    private Member member;
    private Map<String, Object> attributes;

    //일반 로그인
    public MemberContext(Member member) {
        this.member = member;
    }

    //OAuth 로그인
    public MemberContext(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    // 해당 Member의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        // 우리 사이트에서 회원이 1년동안 로그인을 안하면 휴면계정으로 하기로함.
        // member.getLoginDate() 가져와서 현재시간 - 로그인시간 => 1년 초과하면 return false 설정
        // 이런거 해주는것임

        return true;
    }

    /**
     *
     * Oauth2User overloading
     */

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
