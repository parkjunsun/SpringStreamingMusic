package js.StreamingMusic.service;

import js.StreamingMusic.domain.entity.Member;
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
        memberRepository.save(member);
    }

    @Transactional
    public void removeMember(Member member) {
        memberRepository.remove(member);
    }

    public Member findById(Long id) {
        return memberRepository.findOne(id);
    }

    public List<Member> findByUsername(String username) {
        return memberRepository.findByUserName(username);
    }

    public List<Member> findUserContaining(String username) {
        return memberRepository.findByUsernameContaining(username);
    }

    /**
     *
     * 임시로 만든 함수
     */

    public List<Member> findByAdminName(String name) {
        return memberRepository.findByAdminName(name);
    }

    public boolean validateDuplicateMember(String username) {
        List<Member> members = memberRepository.checkByUserName(username);
        if (!members.isEmpty()) {
            return true;
        }

        return false;
    }

    @Transactional
    public void updateMember(String username, String password, String email, String age) {
        Member findMember = memberRepository.findByUserName(username).get(0);
        findMember.setPassword(password);
        findMember.setEmail(email);
        findMember.setAge(age);
    }

    @Transactional
    public void updateMemberRole(Long id, String role) {
        Member member = memberRepository.findOne(id);
        member.setRole(role);
    }

    public List<Member> findAllMember() {
        return memberRepository.findAll();
    }
}
