package js.StreamingMusic.controller;

import js.StreamingMusic.domain.dto.MemberDto;
import js.StreamingMusic.domain.entity.Member;
import js.StreamingMusic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;

    @GetMapping("/admin")
    public String adminHome(Model model) {

        List<Member> members = memberService.findAllMember();
        List<MemberDto> memberList = members.stream()
                .map(m -> new MemberDto(m.getId(), m.getUsername(), m.getEmail(), m.getAge(), m.getRole(), m.getSongQuantity(), m.getBoardQuantity(), m.getJoinDate()))
                .collect(Collectors.toList());


        model.addAttribute("members", memberList);

        return "adminHome";
    }

    @PostMapping("/admin/{id}/delete")
    public String deleteMember(@PathVariable Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Member findMember = memberService.findById(id);
        memberService.removeMember(findMember);

        return "redirect:" + request.getHeader("Referer");
    }
}
