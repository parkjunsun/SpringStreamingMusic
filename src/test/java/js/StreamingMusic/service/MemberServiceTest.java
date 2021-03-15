package js.StreamingMusic.service;

import js.StreamingMusic.domain.entity.Member;
import js.StreamingMusic.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void 회원가입() throws Exception{
        Member member = new Member();
        member.setUsername("park");
        member.setPassword("1234");

        memberService.join(member);

        Member m = memberService.findByUsername("park").get(0);
        System.out.println(m.getUsername());
    }
}