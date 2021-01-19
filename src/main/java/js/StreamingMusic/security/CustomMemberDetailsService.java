package js.StreamingMusic.security;

import js.StreamingMusic.domain.Member;
import js.StreamingMusic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberService.findByUsername(username);

        if (member == null) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));

        MemberContext memberContext = new MemberContext(member, roles);

        return memberContext;
    }
}
