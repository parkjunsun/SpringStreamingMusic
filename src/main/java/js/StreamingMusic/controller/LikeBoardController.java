package js.StreamingMusic.controller;

import js.StreamingMusic.domain.LikeBoardStatus;
import js.StreamingMusic.domain.entity.Board;
import js.StreamingMusic.domain.entity.LikeBoard;
import js.StreamingMusic.domain.entity.Member;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.BoardService;
import js.StreamingMusic.service.LikeBoardService;
import js.StreamingMusic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class LikeBoardController {

    private final MemberService memberService;
    private final BoardService boardService;
    private final LikeBoardService likeBoardService;

    @PostMapping("/like/{id}/add")
    public String addLikeBoard(@PathVariable Long id, @AuthenticationPrincipal MemberContext member, HttpServletRequest request) {

        Member findMember = memberService.findByUsername(member.getUsername());
        Board board = boardService.findBoard(id);

        board.addLikeCount(1);

        LikeBoard likeBoard = new LikeBoard();
        likeBoard.setMember(findMember);
        likeBoard.setBoard(board);

        likeBoardService.saveLike(likeBoard);

        return "redirect:" + request.getHeader("Referer");
    }

}
