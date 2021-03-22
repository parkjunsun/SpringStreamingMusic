package js.StreamingMusic.controller;

import js.StreamingMusic.domain.dto.MemberDto;
import js.StreamingMusic.domain.entity.Board;
import js.StreamingMusic.domain.entity.Member;
import js.StreamingMusic.service.BoardService;
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
    private final BoardService boardService;

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
    public String updateRoleForm(Model model, @PathVariable Long id) {
        Member m = memberService.findById(id);
        MemberDto memberDto = new MemberDto(m.getId(), m.getUsername(), m.getEmail(), m.getAge(), m.getRole(), m.getSongQuantity(), m.getBoardQuantity(), m.getJoinDate());
        model.addAttribute("member", memberDto);

        return "admin/updateRoleForm";
    }

    @PostMapping("/admin/{id}/update")
    public String updateRole(Model model, @PathVariable Long id, @RequestParam("level") String level) {
        memberService.updateMemberRole(id, level);
        return "redirect:/admin";
    }


    @PostMapping("/admin/{id}/delete")
    public String deleteMember(@PathVariable Long id, HttpServletRequest request) {
        Member findMember = memberService.findById(id);
        memberService.removeMember(findMember);

        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/admin/{id}/post_delete")
    public String deleteBoard(@PathVariable Long id, @RequestParam("board_writer") String writer, HttpServletRequest request) {
        Member findMember = memberService.findByUsername(writer).get(0);
        findMember.removeBoard(1);

        Board board = boardService.findBoard(id);
        boardService.removePost(board);

        return "redirect:" + request.getHeader("Referer");

    }
}
