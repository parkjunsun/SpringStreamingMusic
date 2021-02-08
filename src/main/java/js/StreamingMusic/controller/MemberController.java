package js.StreamingMusic.controller;

import js.StreamingMusic.domain.Member;
import js.StreamingMusic.domain.MemberForm;
import js.StreamingMusic.service.MemberService;
import js.StreamingMusic.validate.MemberFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MemberFormValidator memberFormValidator;

    @GetMapping("/register")
    public String createMemberForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/register")
    public String createMember(@Valid MemberForm memberForm, BindingResult result, Errors errors) {

        memberFormValidator.validate(memberForm, errors);

        if (errors.hasErrors()) {
            return "members/createMemberForm";
        }

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

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
