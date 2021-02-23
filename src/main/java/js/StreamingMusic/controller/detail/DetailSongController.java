package js.StreamingMusic.controller.detail;

import js.StreamingMusic.domain.Board;
import js.StreamingMusic.domain.BoardDto;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.BoardService;
import js.StreamingMusic.service.crawling.GetDetailSongInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DetailSongController {

    private final GetDetailSongInfo getDetailSongInfo;
    private final BoardService boardService;

    @GetMapping("/detail/songinfo")
    public String songinfo(Model model, @RequestParam("song_id") String song_id, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        String name = "";
        if (principal != null && principal instanceof MemberContext) {
            name = ((MemberContext) principal).getUsername();
            model.addAttribute("name", name);
        }
        HashMap<String, String> info = getDetailSongInfo.getData(song_id);
        model.addAttribute("info", info);

        List<Board> boardList = boardService.getBoardList(pageNum, song_id);
        List<Integer> pageList = boardService.getPageList(pageNum, song_id);
        Long boardCount = boardService.getBoardCount(song_id);
        List<BoardDto> result = new ArrayList<>();
        for (Board board : boardList) {
            BoardDto boardDto = new BoardDto();
            boardDto.setId(board.getId());
            boardDto.setComment(board.getComment());
            boardDto.setCreatedDate(board.getCreatedDate());
            boardDto.setWriter(board.getMember().getUsername());
            boardDto.setTraceId(board.getTraceId());

            result.add(boardDto);
        }

        model.addAttribute("boardList", result);
        model.addAttribute("allCount", boardCount);
        model.addAttribute("pageList", pageList);
        model.addAttribute("song_id", song_id);
        model.addAttribute("curPage", pageNum);
        model.addAttribute("username", name);

        return "detail/songinfo";
    }
}
