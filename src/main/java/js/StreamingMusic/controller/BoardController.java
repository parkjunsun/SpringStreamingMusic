package js.StreamingMusic.controller;

import js.StreamingMusic.domain.Board;
import js.StreamingMusic.domain.BoardDto;
import js.StreamingMusic.domain.Member;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.BoardService;
import js.StreamingMusic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final MemberService memberService;
    private final BoardService boardService;

    @PostMapping("/post")
    public String writeBoard(BoardDto boardDto, @AuthenticationPrincipal MemberContext member, HttpServletRequest request) {

        Member m = memberService.findByUsername(member.getUsername());

        Board board = new Board();
        board.setComment(boardDto.getComment());
        board.setMember(m);

        boardService.savePost(board);
        return "redirect:" + request.getHeader("Referer");
    }

}
