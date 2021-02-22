package js.StreamingMusic.controller.detail;

import js.StreamingMusic.domain.Board;
import js.StreamingMusic.domain.BoardDto;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.BoardService;
import js.StreamingMusic.service.crawling.GetDetailAlbumInfo;
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
public class DetailAlbumController {

    private final GetDetailAlbumInfo getDetailAlbumInfo;
    private final BoardService boardService;

    @GetMapping("/detail/albuminfo")
    public String GetDetailAlbumInfo(Model model, @RequestParam("album_id") String album_id) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        String name = "";
        if (principal != null && principal instanceof MemberContext) {
            name = ((MemberContext) principal).getUsername();
            model.addAttribute("name", name);
        }

        HashMap<String, String> info = getDetailAlbumInfo.getData(album_id);
        List<HashMap<String, String>> songs = getDetailAlbumInfo.getSongs(album_id);
        List<BoardDto> result = new ArrayList<>();

        List<Board> boardList = boardService.getBoardList();
        for (Board board : boardList) {
            BoardDto boardDto = new BoardDto();
            boardDto.setId(board.getId());
            boardDto.setComment(board.getComment());
            boardDto.setCreatedDate(board.getCreatedDate());
            boardDto.setWriter(board.getMember().getUsername());

            result.add(boardDto);
        }


        model.addAttribute("info", info);
        model.addAttribute("songs", songs);
        model.addAttribute("boardList", result);

        return "detail/albuminfo";
    }

}
