package js.StreamingMusic.controller;

import js.StreamingMusic.domain.Member;
import js.StreamingMusic.domain.MemberForm;
import js.StreamingMusic.domain.Record;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.MemberService;
import js.StreamingMusic.service.RecordService;
import js.StreamingMusic.validate.MemberFormValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final RecordService recordService;


    @GetMapping("/user/{username}")
    public String showUserInfo(Model model, @PathVariable("username") String username, @AuthenticationPrincipal MemberContext memberContext) {

        Member member = memberService.findByUsername(username);
        model.addAttribute("name", member.getUsername());
        model.addAttribute("count", member.getSongQuantity());
        model.addAttribute("role", member.getRole());
        model.addAttribute("joinDate", member.getJoinDate());

        List<Record> records = recordService.findAll(username);
        model.addAttribute("records", records);

        return "members/userInfo";
    }

    @GetMapping("/user/{username}/edit")
    public String updateMemberForm(Model model, @PathVariable("username") String username) {

        Member member = memberService.findByUsername(username);

        MemberForm form = new MemberForm();
        form.setPassword(member.getPassword());
        form.setEmail(member.getEmail());
        form.setAge(member.getAge());

        model.addAttribute("memberForm", form);
        return "members/updateMemberForm";

    }

    @PostMapping("/user/{username}/edit")
    public String updateMember(Model model, @Valid MemberForm form, BindingResult result,
                               @PathVariable("username") String username, RedirectAttributes redirectAttributes,
                               HttpServletRequest request) {

        if (result.hasErrors()) {
            return "members/updateMemberForm";
        }

        memberService.updateMember(username, passwordEncoder.encode(form.getPassword()), form.getEmail(), form.getAge());

        redirectAttributes.addFlashAttribute("successMsg", "회원정보가 수정 되었습니다");
        return "redirect:/user/" + username;
    }
}
