package js.StreamingMusic.controller;

import js.StreamingMusic.domain.Member;
import js.StreamingMusic.domain.Song;
import js.StreamingMusic.exception.DuplicateSongException;
import js.StreamingMusic.exception.NotExistSearchResult;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.MemberService;
import js.StreamingMusic.service.SongService;
import js.StreamingMusic.service.data.DataApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class YoutubeController {

    private final DataApi dataApi;
    private final SongService songService;
    private final MemberService memberService;

    @PostMapping("/youtube")
    public String addPlaylist(Model model,
                              @AuthenticationPrincipal MemberContext memberContext,
                              @RequestParam(value = "add", required = false) String videoId,
                              RedirectAttributes redirectAttributes,
                              HttpServletRequest request) throws IOException, ParseException {

        if (videoId == null) {
            redirectAttributes.addFlashAttribute("errorMsg", "videoId를 입력해주세요");
            return "redirect:" + request.getHeader("Referer");
        }

        String username = memberContext.getUsername();
        Member member = memberService.findByUsername(username);

        HashMap<String, String> videoData = dataApi.getVideoInfo(videoId);
        String title = videoData.get("title");
        String img = videoData.get("img");
        String duration = videoData.get("duration");


        Song song = new Song();
        song.setTitle(title);
        song.setArtist("youtube");
        song.setImg(img);
        song.setVideoId(videoId);
        song.setVideoId2("");
        song.setVideoId3("");
        song.setGenre("youtube");
        song.setDuration(duration);
        song.setMember(member);

        songService.addSong(song, username, title, song.getArtist());

        redirectAttributes.addFlashAttribute("successMsg", "플레이리스트에 추가 되었습니다.");
        return "redirect:" + request.getHeader("Referer");

    }

    @ExceptionHandler
    public String NotExistSearchResultExceptionHandler(NotExistSearchResult e, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        return "redirect:" + request.getHeader("Referer");
    }

    @ExceptionHandler
    public String duplicateSongExceptionHandler(DuplicateSongException e, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        return "redirect:" + request.getHeader("Referer");
    }
}
