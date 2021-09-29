package js.StreamingMusic.security;

import js.StreamingMusic.domain.entity.Member;
import js.StreamingMusic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//@Service("userDetailsService")
//@RequiredArgsConstructor
//public class CustomMemberDetailsService implements UserDetailsService {
//
//    private final MemberService memberService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        List<Member> members = memberService.findByUsername(username);
//        if (members.isEmpty()) {
//            throw new UsernameNotFoundException("UsernameNotFoundException");
//        }
//
//        Member member = members.get(0);
//
//        List<GrantedAuthority> roles = new ArrayList<>();
//        roles.add(new SimpleGrantedAuthority(member.getRole()));
//
//        MemberContext memberContext = new MemberContext(member, roles);
//
//        return memberContext;
//    }
//}

@Service
public class PrincipalMemberDetailsService implements UserDetailsService {

    @Autowired
    private MemberService memberService;

    // 파라미터의 username의 이름은 login_proc의 form안에 있는 name tag와 이름이 같아야한다
    // 시큐리티 session(Authentication(내부 UserDetails)) <= Authentication(내부 UserDetails) <= UserDetails
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Member> memberList = memberService.findByUsername(username);

        if (memberList.size() == 0) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        } else {
            Member member = memberList.get(0);
            return new MemberContext(member);
        }
    }
}
