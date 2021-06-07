package js.StreamingMusic.controller;

import js.StreamingMusic.domain.entity.Member;
import js.StreamingMusic.domain.dto.MemberForm;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public String createMember(@Valid MemberForm memberForm, BindingResult result, Errors errors, RedirectAttributes redirectAttributes) {

        memberFormValidator.validate(memberForm, errors);

        if (errors.hasErrors()) {    //이름 중복검사 (중복 검사하기 위해 validator class를 사용)
            return "members/createMemberForm";
        }

        if (result.hasErrors()) {   // 나머지 오류 검사 (나머지는 bean validataion의 어노테이션이 있기 때문에 validator class 사용 안함)
            return "members/createMemberForm";
        }

        Member member = new Member();
        member.setUsername(memberForm.getUsername());
        member.setPassword(passwordEncoder.encode(memberForm.getPassword()));
        member.setEmail(memberForm.getEmail());
        member.setAge(memberForm.getAge());
        member.setRole(memberForm.getRole());
        member.setJoinDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        memberService.join(member);

        redirectAttributes.addFlashAttribute("successMsg", "회원가입이 완료되었습니다. 로그인 해주세요");
        return "redirect:/";
    }

}
