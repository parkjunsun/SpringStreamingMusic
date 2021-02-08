package js.StreamingMusic.service;

import js.StreamingMusic.domain.Member;
import js.StreamingMusic.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;


    @Transactional
    public void join(Member member) {
//        validateDuplicateMember(member);
        memberRepository.save(member);
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUserName(username);
    }

    public boolean validateDuplicateMember(String username) {
        List<Member> members = memberRepository.checkByUserName(username);
        if (!members.isEmpty()) {
            return true;
        }

        return false;
    }

}
