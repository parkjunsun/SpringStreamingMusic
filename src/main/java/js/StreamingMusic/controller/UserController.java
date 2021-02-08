package js.StreamingMusic.controller;

import js.StreamingMusic.domain.Member;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;

    @GetMapping("/user/{username}")
    public String showUserInfo(Model model, @PathVariable("username") String username, @AuthenticationPrincipal MemberContext memberContext) {

        Member member = memberService.findByUsername(username);
        return "members/userInfo";
    }

//    @GetMapping("/user/{username}/edit")
//    public String updateMemberForm(Model model, @PathVariable("username") String username) {
//
//    }
}
