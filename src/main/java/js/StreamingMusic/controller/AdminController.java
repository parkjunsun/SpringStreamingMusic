package js.StreamingMusic.controller;

import js.StreamingMusic.domain.Role;
import js.StreamingMusic.domain.dto.MemberDto;
import js.StreamingMusic.domain.entity.Member;
import js.StreamingMusic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

        return "admin/adminHome";
    }


    @GetMapping("/admin/userSearch")
    public String adminSearch(Model model, @RequestParam("username") String username) {
        List<Member> members = memberService.findUserContaining(username);
        List<MemberDto> memberList = members.stream()
                .map(m -> new MemberDto(m.getId(), m.getUsername(), m.getEmail(), m.getAge(), m.getRole(), m.getSongQuantity(), m.getBoardQuantity(), m.getJoinDate()))
                .collect(Collectors.toList());

        model.addAttribute("members", memberList);

        return "admin/searchResult";
    }


    @GetMapping("/admin/{id}/updateRole")
    public String updateRoleForm(Model model, @PathVariable Long id, @ModelAttribute("role") Role role) {
        Member m = memberService.findById(id);
        MemberDto memberDto = new MemberDto(m.getId(), m.getUsername(), m.getEmail(), m.getAge(), m.getRole(), m.getSongQuantity(), m.getBoardQuantity(), m.getJoinDate());
        model.addAttribute("member", memberDto);

        return "admin/updateRole";
    }


    @PostMapping("/admin/{id}/delete")
    public String deleteMember(@PathVariable Long id, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Member findMember = memberService.findById(id);
        memberService.removeMember(findMember);

        return "redirect:" + request.getHeader("Referer");
    }
}
