package js.StreamingMusic.controller;

import js.StreamingMusic.domain.Board;
import js.StreamingMusic.domain.BoardDto;
import js.StreamingMusic.domain.Member;
import js.StreamingMusic.exception.BoardInputEmptyException;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.BoardService;
import js.StreamingMusic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final MemberService memberService;
    private final BoardService boardService;

    @PostMapping("/post")
    public String writeBoard(BoardDto boardDto, @AuthenticationPrincipal MemberContext member, HttpServletRequest request) {

        Member m = memberService.findByUsername(member.getUsername());
        m.addBoard(1);

        Board board = new Board();
        board.setComment(boardDto.getComment());
        board.setTraceId(boardDto.getTraceId());
        board.setTitle(boardDto.getTitle());
        board.setArtist(boardDto.getArtist());
        board.setImg(boardDto.getImg());
        board.setMember(m);

        boardService.savePost(board);
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/post/{id}/delete")
    public String deleteBoard(@PathVariable Long id, @AuthenticationPrincipal MemberContext member, HttpServletRequest request) {
        Member m = memberService.findByUsername(member.getUsername());
        m.removeBoard(1);

        Board board = boardService.findBoard(id);
        boardService.removePost(board);

        return "redirect:" + request.getHeader("Referer");
    }

    @ExceptionHandler
    public String BoardInputEmptyExceptionHandler(BoardInputEmptyException e, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        return "redirect:" + request.getHeader("Referer");
    }



}
