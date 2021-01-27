package js.StreamingMusic.controller;

import js.StreamingMusic.domain.Member;
import js.StreamingMusic.domain.MemberForm;
import js.StreamingMusic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String createMemberForm(Model model) {
        return "members/createMemberForm";
    }

    @PostMapping("/register")
    public String createMember(MemberForm memberForm) {
        Member member = new Member();
        member.setUsername(memberForm.getUsername());
        member.setPassword(passwordEncoder.encode(memberForm.getPassword()));
        member.setEmail(memberForm.getEmail());
        member.setAge(memberForm.getAge());
        member.setRole(memberForm.getRole());

        memberService.join(member);

        return "redirect:/";
    }
}
