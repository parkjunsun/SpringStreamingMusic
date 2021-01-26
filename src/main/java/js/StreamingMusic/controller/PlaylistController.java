package js.StreamingMusic.controller;

import js.StreamingMusic.domain.Member;
import js.StreamingMusic.domain.Song;
import js.StreamingMusic.exception.AppError;
import js.StreamingMusic.exception.DuplicateSongException;
import js.StreamingMusic.security.MemberContext;
import js.StreamingMusic.service.MemberService;
import js.StreamingMusic.service.SongService;
import js.StreamingMusic.service.crawling.GetHomeNewAlbums;
import js.StreamingMusic.service.crawling.GetTop10;
import js.StreamingMusic.service.data.DataApi;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PlaylistController {

    private final MemberService memberService;
    private final DataApi dataApi;
    private final SongService songService;

    @GetMapping("/playlist")
    public String showPlaylist(Model model, @AuthenticationPrincipal MemberContext member) {

        if (member != null) {
            String username = member.getUsername();
            model.addAttribute("name", username);
        }

        List<Song> songs = songService.findAllSongsByName(member.getUsername());
        model.addAttribute("songs", songs);

        return "playlist";
    }

    @PostMapping("/playlist")
    public String SavePlaylist(Model model, @AuthenticationPrincipal MemberContext member,
                                            @RequestParam("add") List<String> param,
                                            RedirectAttributes redirectAttributes) throws IOException, ParseException {

        String username = member.getUsername();
        Member m = memberService.findByUsername(username);

        if (param.size() == 1) {
            String[] params = param.get(0).split(";");
            String title = params[0];
            String artist = params[1];

            List<String> videoIds = dataApi.getVideoId(title, artist);

            String videoId = videoIds.get(0);
            String videoId2 = videoIds.get(1);
            String videoId3 = videoIds.get(2);

            String img = dataApi.getImg(title, artist);
            img = "https:" + img;

            List<String> details = dataApi.getDetail(title, artist);

            String genre = details.get(0);
            String duration = details.get(1);

            Song song = new Song();
            song.setTitle(title);
            song.setArtist(artist);
            song.setImg(img);
            song.setVideoId(videoId);
            song.setVideoId2(videoId2);
            song.setVideoId3(videoId3);
            song.setGenre(genre);
            song.setDuration(duration);

            song.setMember(m);

            try {
                songService.addSong(song);
            } catch (DuplicateSongException e) {
                redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
                return "redirect:/";
            }
        }

        redirectAttributes.addFlashAttribute("successMsg", "플레이리스트에 추가 되었습니다");
        return "redirect:/";
    }

}
